package sperka.online.bookcase.server.service.impl;

import io.jsonwebtoken.Jwts;
import sperka.online.bookcase.server.auth.SigningKey;
import sperka.online.bookcase.server.entity.User;
import sperka.online.bookcase.server.repository.UserRepository;
import sperka.online.bookcase.server.service.AuthService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;

    @Inject
    public AuthServiceImpl( UserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public boolean createUser( String username, String password, String roles ) {
        User user = null;
        try {
            user = userRepository.getUserByUsername( username );
        } catch ( Exception ignored ) {
        }

        if ( user == null ) {
            userRepository.save( User.create( username, password, roles ) );
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
    public boolean modifyUser( String username, String password, List< String > roles ) {
        if ( username == null ) {
            return false;
        }

        var user = userRepository.getUserByUsername( username );
        if ( user != null ) {
            if ( password != null && password.length() > 4 ) {
                user.setPassword( password );
            }
            if ( roles != null && roles.size() > 0 ) {
                user.setRoles( String.join( "", roles ) );
            }

            userRepository.save( user );

            return true;
        }

        return false;
    }

    @Override
    public boolean deleteUser( String username, String currentUser ) {
        if ( username == null ) {
            return false;
        }
        if ( currentUser.equals( username ) ) {
            return false; // User cannot remove himself
        }

        var user = userRepository.getUserByUsername( username );
        if ( user != null ) {
            userRepository.delete( user );
            return true;
        }

        return false;
    }
}
