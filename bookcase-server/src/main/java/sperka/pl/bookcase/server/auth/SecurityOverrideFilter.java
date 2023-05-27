package sperka.pl.bookcase.server.auth;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import sperka.pl.bookcase.server.service.AuthService;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Slf4j
@Provider
@PreMatching
public class SecurityOverrideFilter implements ContainerRequestFilter {
    private final AuthService authService;

    @Inject
    public SecurityOverrideFilter( AuthService authService ) {
        this.authService = authService;
    }

    @Override
    public void filter( ContainerRequestContext containerRequestContext ) throws IOException {

        String authorization = containerRequestContext.getHeaders().getFirst( "Authorization" );
        if ( authorization != null && authorization.startsWith( "Bearer " ) ) {
            try {
                var token = authorization.substring( 7 );

                if ( authService.isBlacklisted( token ) ) {
                    containerRequestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
                }

                var jwt = Jwts.parserBuilder().setSigningKey( SigningKey.getKey() ).build().parseClaimsJws( token );
                containerRequestContext.setSecurityContext( new JwtSecurityContext( jwt, containerRequestContext.getSecurityContext() ) );
            } catch ( Exception exception ) {
                log.error( authorization );
                log.error( exception.toString() );
                containerRequestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
            }
        }
    }
}
