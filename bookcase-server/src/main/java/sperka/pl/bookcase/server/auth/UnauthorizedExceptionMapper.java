package sperka.pl.bookcase.server.auth;

import io.quarkus.security.UnauthorizedException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority( Priorities.AUTHORIZATION )
public class UnauthorizedExceptionMapper implements ExceptionMapper< UnauthorizedException > {
    @Context
    private SecurityContext securityContext;

    @Override
    public Response toResponse( UnauthorizedException e ) {
        return Response.status( Response.Status.UNAUTHORIZED ).build();
    }
}
