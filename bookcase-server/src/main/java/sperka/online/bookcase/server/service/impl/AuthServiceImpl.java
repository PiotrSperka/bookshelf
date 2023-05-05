package sperka.online.bookcase.server.service.impl;

import io.jsonwebtoken.Jwts;
import sperka.online.bookcase.server.auth.SigningKey;
import sperka.online.bookcase.server.dto.UserInfoDto;
import sperka.online.bookcase.server.entity.User;
import sperka.online.bookcase.server.repository.UserRepository;
import sperka.online.bookcase.server.service.AuthService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;

    @Inject
    public AuthServiceImpl( UserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean initializeUsers() {
        if ( countUsers() > 0 ) {
            return false;
        }

        createUser( "user", "user", Collections.singletonList( "user" ) );
        createUser( "admin", "admin", Collections.singletonList( "admin" ) );

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
            userRepository.save( User.create( username, password, String.join( ",", roles ) ) );
            return true;
        }

        return false;
    }

    @Override
    public String loginUser( String username, String password ) {
        if ( username == null || password == null ) {
            return null;
        }

        var user = userRepository.getUserByUsername( username );
        if ( user != null && user.isPasswordMatching( password ) ) {
            return Jwts.builder()
                    .setIssuer( "Bookstack" )
                    .setSubject( user.getName() )
                    .claim( "roles", user.getRoles() )
                    .setIssuedAt( Date.from( Instant.now() ) )
                    .setExpiration( Date.from( Instant.now().plusMillis( 1000 * 60 * 60 ) ) )
                    .signWith( SigningKey.getKey() )
                    .compact();
        }

        return null;
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
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean modifyUser( Long id, String username, String password, List< String > roles ) {
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

            userRepository.save( user );

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
            return true;
        }

        return false;
    }
}
