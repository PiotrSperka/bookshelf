package sperka.pl.bookcase.server.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sperka.pl.bookcase.server.dto.UserInfoDto;
import sperka.pl.bookcase.server.entity.User;
import sperka.pl.bookcase.server.exceptions.ValidationException;
import sperka.pl.bookcase.server.mailer.MailerFacade;
import sperka.pl.bookcase.server.repository.UserRepository;
import sperka.pl.bookcase.server.service.LogService;
import sperka.pl.bookcase.server.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LogService logService;
    private final MailerFacade mailer;

    public UserServiceImpl( UserRepository userRepository, LogService logService, MailerFacade mailerFacade ) {
        this.userRepository = userRepository;
        this.logService = logService;
        this.mailer = mailerFacade;
    }

    @Override
    public boolean initializeUsers() {
        if ( countUsers() > 0 ) {
            return false;
        }

        if ( createUser( "admin", Collections.singletonList( "admin" ), "change@me", "" ) ) {
            var user = userRepository.getUserByUsername( "admin" );
            user.setPassword( "ChangeMe!" );
            user.setResetPasswordToken( null );
            userRepository.save( user );

            logService.add( "Initialized users table", "system" );
            return true;
        }

        return false;
    }

    @Override
    public List< UserInfoDto > getAll() {
        return userRepository.getAll().stream().map( UserInfoDto::fromEntity ).collect( Collectors.toList() );
    }

    @Override
    public UserInfoDto get( Long id ) {
        var user = userRepository.getById( id );
        return user != null ? UserInfoDto.fromEntity( user ) : null;
    }

    @Override
    public UserInfoDto getByUsername( String username ) {
        var user = userRepository.getUserByUsername( username );
        return user != null ? UserInfoDto.fromEntity( user ) : null;
    }

    @Override
    public long countUsers() {
        return userRepository.countAll();
    }

    @Override
    @Transactional
    public boolean createUser( String username, List< String > roles, String email, String locale ) {
        User user = null;
        try {
            user = userRepository.getUserByUsername( username );
        } catch ( Exception ignored ) {
        }

        if ( user == null ) {
            user = User.create( username, String.join( ",", roles ), email, locale );
            user.emptyPassword();
            user.setResetPasswordToken( getRandomString( 64 ) );

            userRepository.save( user );
            logService.add( "Created new user " + user, "system" );
            mailer.sendWelcomeMail( user );
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean modifyPassword( String username, String oldPassword, String newPassword ) {
        if ( username == null || oldPassword == null || newPassword == null ) {
            return false;
        }
        if ( oldPassword.equals( newPassword ) ) {
            return false;
        }
        if ( newPassword.length() < 4 ) {
            return false;
        }

        var user = userRepository.getUserByUsername( username );
        if ( user.isPasswordMatching( oldPassword ) ) {
            user.setPassword( newPassword );
            userRepository.save( user );
            logService.add( "Changed password for user id = " + user.getId() + " named '" + user.getName() + "'", "system" );
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean modifyUser( Long id, String username, String password, List< String > roles, Boolean active, String email, String locale ) {
        if ( id == null ) {
            return false;
        }

        var user = userRepository.getById( id );
        if ( user != null ) {
            if ( password != null && password.length() > 4 ) {
                user.setPassword( password );
            }
            if ( roles != null ) {
                user.setRoles( String.join( ",", roles ) );
            }
            if ( username != null && !username.isEmpty() ) {
                user.setName( username );
            }
            if ( active != null ) {
                user.setActive( active );
            }
            if ( email != null ) {
                user.setEmail( email );
            }
            if ( locale != null ) {
                user.setLocale( locale );
            }

            userRepository.save( user );
            logService.add( "Modified user " + user, "system" );

            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean deleteUser( Long id, String currentUsername ) {
        var currentUser = userRepository.getUserByUsername( currentUsername );
        if ( id == null || currentUser == null ) {
            return false;
        }
        if ( currentUser.getId().equals( id ) ) {
            return false; // User cannot remove himself
        }

        var user = userRepository.getById( id );
        if ( user != null ) {
            userRepository.delete( user );
            logService.add( "Deleted user " + user, "system" );
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean resetPassword( String token, String password, String passwordRepeat ) {
        var validationException = new ValidationException();
        User user = userRepository.getUserByResetPasswordToken( token );

        if ( user == null ) {
            validationException.addViolation( "generic", "reset-password.error.token-error" );
        }

        if ( password.isBlank() ) {
            validationException.addViolation( "password", "reset-password.error.password-cannot-be-blank" );
        }

        if ( !password.equals( passwordRepeat ) ) {
            validationException.addViolation( "password", "reset-password.error.password-repeat-does-not-match" );
        }

        if ( !validationException.isEmpty() ) {
            throw validationException;
        }

        if ( user != null ) {
            user.setResetPasswordToken( null );
            user.setPassword( password );
            userRepository.save( user );
            return true;
        }

        return false;
    }

    private String getRandomString( int length ) {
        final int leftLimit = 48; // numeral '0'
        final int rightLimit = 122; // letter 'z'
        return new Random().ints( leftLimit, rightLimit + 1 )
                .filter( i -> ( i <= 57 || i >= 65 ) && ( i <= 90 || i >= 97 ) )
                .limit( length )
                .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                .toString();
    }
}
