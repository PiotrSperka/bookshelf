package sperka.pl.bookcase.server.resource;

import jakarta.annotation.security.RolesAllowed;
import sperka.pl.bookcase.server.auth.Roles;
import sperka.pl.bookcase.server.dto.LogFilterDto;
import sperka.pl.bookcase.server.service.LogService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path( "/api/logs" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
@RolesAllowed( Roles.ADMIN )
public class LogResource {
    private final LogService logService;

    public LogResource( LogService logService ) {
        this.logService = logService;
    }

    @POST
    @Path( "/page/{page}/{perPage}" )
    public Response getPaginated( @PathParam( "page" ) int page, @PathParam( "perPage" ) int perPage, LogFilterDto filters ) {
        return Response.ok().entity( logService.getPaginated( page, perPage, filters ) ).build();
    }

    @POST
    @Path( "/count" )
    public Response count( LogFilterDto filters ) {
        return Response.ok().entity( logService.getCount( filters ) ).build();
    }
}
