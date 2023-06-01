package sperka.pl.bookcase.server.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import sperka.pl.bookcase.server.auth.SigningKey;
import sperka.pl.bookcase.server.entity.JwtBlacklist;
import sperka.pl.bookcase.server.entity.User;
import sperka.pl.bookcase.server.repository.JwtBlacklistRepository;
import sperka.pl.bookcase.server.repository.UserRepository;
import sperka.pl.bookcase.server.service.AuthService;
import sperka.pl.bookcase.server.service.LogService;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final UserRepository userRepository;
    private final LogService logService;
    private final SigningKey signingKey;

    public AuthServiceImpl( JwtBlacklistRepository jwtBlacklistRepository, UserRepository userRepository, LogService logService, SigningKey signingKey ) {
        this.jwtBlacklistRepository = jwtBlacklistRepository;
        this.userRepository = userRepository;
        this.logService = logService;
        this.signingKey = signingKey;
    }

    @Override
    public String loginUser( String username, String password ) {
        if ( username == null || password == null ) {
            return null;
        }

        var user = userRepository.getUserByUsername( username );
        if ( user != null && user.isPasswordMatching( password ) ) {
            if ( user.getActive() ) {
                logService.add( "User '" + username + "' authenticated successfully", "system" );
                return getJwtToken( user );
            } else {
                logService.add( "User '" + username + "' is inactive and cannot be logged in", "system" );
                return null;
            }
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
        var jwt = Jwts.parserBuilder().setSigningKey( signingKey.getKey() ).build().parseClaimsJws( token );
        var user = userRepository.getUserByUsername( jwt.getBody().getSubject() );
        if ( user != null && user.getActive() ) {
            if ( jwt.getBody().getExpiration().toInstant().isBefore( Instant.now().plus( 5, ChronoUnit.MINUTES ) ) ) {
                blacklist( token );
                return getJwtToken( user );
            } else {
                return token;
            }
        }

        return null;
    }

    private String getJwtToken( User user ) {
        return Jwts.builder()
                .setIssuer( "Bookshelf" )
                .setSubject( user.getName() )
                .claim( "roles", user.getRoles() )
                .setIssuedAt( Date.from( Instant.now() ) )
                .setExpiration( Date.from( Instant.now().plusMillis( 1000 * 60 * 60 ) ) )
                .signWith( signingKey.getKey() )
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
