package sperka.pl.bookcase.server.auth;

import io.quarkus.arc.Priority;
import io.quarkus.security.UnauthorizedException;

import javax.ws.rs.Priorities;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

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
