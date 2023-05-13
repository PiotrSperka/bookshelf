package sperka.online.bookcase.server.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import sperka.online.bookcase.server.auth.SigningKey;
import sperka.online.bookcase.server.entity.JwtBlacklist;
import sperka.online.bookcase.server.entity.User;
import sperka.online.bookcase.server.repository.JwtBlacklistRepository;
import sperka.online.bookcase.server.repository.UserRepository;
import sperka.online.bookcase.server.service.AuthService;
import sperka.online.bookcase.server.service.LogService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final UserRepository userRepository;
    private final LogService logService;

    public AuthServiceImpl( JwtBlacklistRepository jwtBlacklistRepository, UserRepository userRepository, LogService logService ) {
        this.jwtBlacklistRepository = jwtBlacklistRepository;
        this.userRepository = userRepository;
        this.logService = logService;
    }

    @Override
    public String loginUser( String username, String password ) {
        if ( username == null || password == null ) {
            return null;
        }

        var user = userRepository.getUserByUsername( username );
        if ( user != null && user.isPasswordMatching( password ) ) {
            logService.add( "User '" + username + "' authenticated successfully", "system" );
            return getJwtToken( user );
        }

        logService.add( "Failed to authenticate user '" + username + "'", "system" );

        return null;
    }

    @Override
    @Transactional
    public void blacklist( String jwt ) {
        if ( jwtBlacklistRepository.countByToken( jwt ) == 0 ) {
            var blacklist = new JwtBlacklist();
            blacklist.setJwt( jwt );
            jwtBlacklistRepository.save( blacklist );
        }
    }

    @Override
    public boolean isBlacklisted( String jwt ) {
        return jwtBlacklistRepository.countByToken( jwt ) > 0;
    }

    @Override
    public String refresh( String token ) {
        var jwt = Jwts.parserBuilder().setSigningKey( SigningKey.getKey() ).build().parseClaimsJws( token );
        var user = userRepository.getUserByUsername( jwt.getBody().getSubject() );
        if ( user != null ) {
            if ( jwt.getBody().getExpiration().toInstant().isBefore( Instant.now().plus( 5, ChronoUnit.MINUTES ) ) ) {
                blacklist( token );
                return getJwtToken( user );
            } else {
                return token;
            }
        }

        return null;
    }

    private static String getJwtToken( User user ) {
        return Jwts.builder()
                .setIssuer( "Bookshelf" )
                .setSubject( user.getName() )
                .claim( "roles", user.getRoles() )
                .setIssuedAt( Date.from( Instant.now() ) )
                .setExpiration( Date.from( Instant.now().plusMillis( 1000 * 60 * 60 ) ) )
                .signWith( SigningKey.getKey() )
                .compact();
    }

    @Scheduled( every = "10m" )
    @Transactional
    void blacklistCleanup() {
        var tokens = jwtBlacklistRepository.getAll();
        for ( var token : tokens ) {
            var jwt = token.getJwt();
            var jwtWithoutSigning = jwt.substring( 0, jwt.lastIndexOf( '.' ) + 1 );
            try {
                Jwts.parserBuilder().build().parse( jwtWithoutSigning );
            } catch ( ExpiredJwtException ex ) {
                jwtBlacklistRepository.delete( token );
            }
        }
    }
}
