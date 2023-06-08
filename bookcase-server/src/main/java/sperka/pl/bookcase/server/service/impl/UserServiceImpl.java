package sperka.pl.bookcase.server.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sperka.pl.bookcase.server.dto.CreateUserRequestDto;
import sperka.pl.bookcase.server.dto.InitializeUserRequestDto;
import sperka.pl.bookcase.server.dto.ModifyUserRequestDto;
import sperka.pl.bookcase.server.dto.UserInfoDto;
import sperka.pl.bookcase.server.entity.User;
import sperka.pl.bookcase.server.exceptions.ValidationException;
import sperka.pl.bookcase.server.mailer.MailerFacade;
import sperka.pl.bookcase.server.repository.UserRepository;
import sperka.pl.bookcase.server.service.LogService;
import sperka.pl.bookcase.server.service.UserService;
import sperka.pl.bookcase.server.validators.UserDtoValidator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LogService logService;
    private final MailerFacade mailer;
    private final UserDtoValidator userDtoValidator;

    public UserServiceImpl( UserRepository userRepository, LogService logService, MailerFacade mailerFacade, UserDtoValidator userDtoValidator ) {
        this.userRepository = userRepository;
        this.logService = logService;
        this.mailer = mailerFacade;
        this.userDtoValidator = userDtoValidator;
    }

    @Override
    @Transactional
    public boolean initializeUser( InitializeUserRequestDto dto ) {
        if ( countUsers() > 0 ) {
            return false;
        }

        var violations = userDtoValidator.validate( dto );
        if ( !violations.isEmpty() ) {
            throw new ValidationException( violations );
        }

        var user = User.create( dto.getName(), "admin", dto.getEmail(), "" );
        user.setPassword( dto.getPassword() );

        userRepository.save( user );
        logService.add( "Created new user " + user, "system" );
        logService.add( "Initialized users table", "system" );

        return true;
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
    public boolean createUser( CreateUserRequestDto dto ) {
        var validationResult = userDtoValidator.validate( dto );

        if ( !validationResult.isEmpty() ) {
            throw new ValidationException( validationResult );
        }

        var user = User.create( dto.getName(), String.join( ",", dto.getRoles() ), dto.getEmail(), dto.getLocale() );
        user.setResetPasswordToken( getRandomString( 64 ) );

        userRepository.save( user );
        logService.add( "Created new user " + user, "system" );
        mailer.sendWelcomeMail( user );

        return true;
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
        if ( newPassword.length() < 6 ) {
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
    public boolean modifyUser( ModifyUserRequestDto dto ) {
        if ( dto.getId() == null ) {
            return false;
        }

        var validationResult = userDtoValidator.validate( dto );

        if ( !validationResult.isEmpty() ) {
            throw new ValidationException( validationResult );
        }

        var user = userRepository.getById( dto.getId() );
        if ( user != null ) {
            if ( dto.getPassword() != null && !dto.getPassword().isBlank() ) {
                user.setPassword( dto.getPassword() );
            }
            if ( dto.getRoles() != null ) {
                user.setRoles( String.join( ",", dto.getRoles() ) );
            }
            if ( dto.getName() != null && !dto.getName().isBlank() ) {
                user.setName( dto.getName() );
            }
            if ( user.getActive() != null ) {
                user.setActive( user.getActive() );
            }
            if ( user.getEmail() != null ) {
                user.setEmail( user.getEmail() );
            }
            if ( user.getLocale() != null ) {
                user.setLocale( user.getLocale() );
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

        if ( password.length() < 6 ) {
            validationException.addViolation( "password", "reset-password.error.password-needs-to-be-6-chars" );
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

    @Override
    @Transactional
    public boolean sendResetPasswordToken( String email ) {
        if ( email == null || email.isBlank() ) {
            return false;
        }

        var user = userRepository.getUserByEmail( email );
        if ( user != null ) {
            user.setResetPasswordToken( getRandomString( 64 ) );
            userRepository.save( user );
            logService.add( "Requested password reset for " + user, "system" );
            mailer.sendPasswordResetMail( user );
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
