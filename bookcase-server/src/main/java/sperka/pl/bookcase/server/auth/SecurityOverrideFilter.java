package sperka.pl.bookcase.server.auth;

import io.jsonwebtoken.Jwts;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import sperka.pl.bookcase.server.service.AuthService;

@Slf4j
@Provider
@PreMatching
public class SecurityOverrideFilter implements ContainerRequestFilter {
    private final AuthService authService;
    private final SigningKey signingKey;

    @Inject
    public SecurityOverrideFilter( AuthService authService, SigningKey signingKey ) {
        this.authService = authService;
        this.signingKey = signingKey;
    }

    @Override
    public void filter( ContainerRequestContext containerRequestContext ) {
        String authorization = containerRequestContext.getHeaders().getFirst( "Authorization" );
        if ( authorization != null && authorization.startsWith( "Bearer " ) ) {
            try {
                var token = authorization.substring( 7 );

                if ( authService.isBlacklisted( token ) ) {
                    containerRequestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
                }

                var jwt = Jwts.parserBuilder().setSigningKey( signingKey.getKey() ).build().parseClaimsJws( token );
                containerRequestContext.setSecurityContext( new JwtSecurityContext( jwt, containerRequestContext.getSecurityContext() ) );
            } catch ( Exception exception ) {
                log.error( authorization );
                log.error( exception.toString() );
                containerRequestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
            }
        }
    }
}
