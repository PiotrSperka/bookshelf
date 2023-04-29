package sperka.online.bookcase.server.auth;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Slf4j
@Provider
@PreMatching
public class SecurityOverrideFilter implements ContainerRequestFilter {
    @Override
    public void filter( ContainerRequestContext containerRequestContext ) throws IOException {

        String authorization = containerRequestContext.getHeaders().getFirst( "Authorization" );
        if ( authorization != null && authorization.startsWith( "Bearer " ) ) {
            try {
                var jwt = Jwts.parserBuilder().setSigningKey( SigningKey.getKey() ).build().parseClaimsJws( authorization.substring( 7 ) );
                containerRequestContext.setSecurityContext( new JwtSecurityContext( jwt, containerRequestContext.getSecurityContext() ) );
            } catch ( Exception exception ) {
                log.error( authorization );
                log.error( exception.toString() );
                containerRequestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
            }
        }
    }
}
