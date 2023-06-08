package sperka.pl.bookcase.server.resource;

import jakarta.annotation.security.RolesAllowed;
import sperka.pl.bookcase.server.auth.Roles;
import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.helpers.InstantParam;
import sperka.pl.bookcase.server.service.BookDatabaseSynchronizationService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path( "api/sync" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
@RolesAllowed( { Roles.ADMIN, Roles.USER } )
public class SynchronizationResource {
    private final BookDatabaseSynchronizationService bookDatabaseSynchronizationService;

    @Inject
    public SynchronizationResource( BookDatabaseSynchronizationService bookDatabaseSynchronizationService ) {
        this.bookDatabaseSynchronizationService = bookDatabaseSynchronizationService;
    }

    @POST
    @Path( "/{lastSyncTime}" )
    public List<BookDto> synchronize(List< BookDto > books, @PathParam( "lastSyncTime" ) InstantParam lastSyncTime ) {
        return bookDatabaseSynchronizationService.synchronize( books, lastSyncTime.getInstant() );
    }
}
