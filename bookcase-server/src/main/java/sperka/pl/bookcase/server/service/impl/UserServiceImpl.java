package sperka.pl.bookcase.server.service.impl;

import sperka.pl.bookcase.server.dto.UserInfoDto;
import sperka.pl.bookcase.server.entity.User;
import sperka.pl.bookcase.server.repository.UserRepository;
import sperka.pl.bookcase.server.service.LogService;
import sperka.pl.bookcase.server.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final LogService logService;

    public UserServiceImpl( UserRepository userRepository, LogService logService ) {
        this.userRepository = userRepository;
        this.logService = logService;
    }

    @Override
    public boolean initializeUsers() {
        if ( countUsers() > 0 ) {
            return false;
        }

        createUser( "admin", "ChangeMe!", Collections.singletonList( "admin" ) );

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
    public boolean createUser( String username, String password, List< String > roles ) {
        User user = null;
        try {
            user = userRepository.getUserByUsername( username );
        } catch ( Exception ignored ) {
        }

        if ( user == null ) {
            user = User.create( username, password, String.join( ",", roles ) );
            userRepository.save( user );
            logService.add( "Created new user " + user, "system" );
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
    public boolean modifyUser( Long id, String username, String password, List< String > roles, Boolean active ) {
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
}
