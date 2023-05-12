package sperka.online.bookcase.server.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import sperka.online.bookcase.server.auth.SigningKey;
import sperka.online.bookcase.server.entity.JwtBlacklist;
import sperka.online.bookcase.server.repository.JwtBlacklistRepository;
import sperka.online.bookcase.server.repository.UserRepository;
import sperka.online.bookcase.server.service.AuthService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;

@ApplicationScoped
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final UserRepository userRepository;

    public AuthServiceImpl( JwtBlacklistRepository jwtBlacklistRepository, UserRepository userRepository ) {
        this.jwtBlacklistRepository = jwtBlacklistRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String loginUser( String username, String password ) {
        if ( username == null || password == null ) {
            return null;
        }

        var user = userRepository.getUserByUsername( username );
        if ( user != null && user.isPasswordMatching( password ) ) {
            return Jwts.builder()
                    .setIssuer( "Bookshelf" )
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
            blacklist( token );
            return Jwts.builder()
                    .setIssuer( "Bookshelf" )
                    .setSubject( user.getName() )
                    .claim( "roles", user.getRoles() )
                    .setIssuedAt( Date.from( Instant.now() ) )
                    .setExpiration( Date.from( Instant.now().plusMillis( 1000 * 60 * 60 ) ) )
                    .signWith( SigningKey.getKey() )
                    .compact();
        }

        return null;
    }

    @Scheduled( every = "10m" )
    @Transactional
    void blacklistCleanup() {
        log.info( "Starting blacklist cleanup" );
        var tokens = jwtBlacklistRepository.getAll();
        for ( var token : tokens ) {
            var jwt = token.getJwt();
            var jwtWithoutSigning = jwt.substring( 0, jwt.lastIndexOf( '.' ) + 1 );
            if ( ( ( Claims ) Jwts.parserBuilder().build().parse( jwtWithoutSigning ).getBody() ).getExpiration().toInstant().isBefore( Instant.now() ) ) {
                log.info( "Deleting token because it expired: " + token );
                jwtBlacklistRepository.delete( token );
            }
        }
        log.info( "Finished blacklist cleanup" );
    }
}
