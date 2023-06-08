package sperka.pl.bookcase.server.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sperka.pl.bookcase.server.dto.GenericResponseDto;
import sperka.pl.bookcase.server.dto.InitializeUserRequestDto;
import sperka.pl.bookcase.server.exceptions.ValidationException;
import sperka.pl.bookcase.server.service.UserService;

@ApplicationScoped
@Path( "/api/install" )
@PermitAll
public class InstallResource {
    private final UserService userService;

    @Inject
    public InstallResource( UserService userService ) {
        this.userService = userService;
    }

    @Path( "/" )
    @GET
    @Produces( MediaType.TEXT_PLAIN )
    public Response allowInstaller() {
        if ( userService.countUsers() == 0 ) {
            return Response.ok().entity( "true" ).build();
        }

        return Response.ok().entity( "false" ).build();
    }

    @Path( "/" )
    @POST
    public Response run( InitializeUserRequestDto dto ) {
        try {
            if ( userService.initializeUser( dto ) ) {
                return Response.ok().entity( new GenericResponseDto( "OK" ) ).build();
            }
        } catch ( ValidationException ex ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity( ex.getViolations() ).build();
        }

        return Response.status( Response.Status.FORBIDDEN ).build();
    }
}
