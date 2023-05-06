package sperka.online.bookcase.server.resource;

import sperka.online.bookcase.server.auth.Roles;
import sperka.online.bookcase.server.dto.BookDto;
import sperka.online.bookcase.server.helpers.InstantParam;
import sperka.online.bookcase.server.service.BookDatabaseSynchronizationService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ApplicationScoped
@Path( "api/sync" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
@RolesAllowed( { Roles.ADMIN, Roles.USER } )
public class SynchronizationResource {
    BookDatabaseSynchronizationService bookDatabaseSynchronizationService;

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
