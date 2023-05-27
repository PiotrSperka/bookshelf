package sperka.pl.bookcase.server.resource;

import jakarta.annotation.security.RolesAllowed;
import sperka.pl.bookcase.server.auth.Roles;
import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.dto.BookFilterDto;
import sperka.pl.bookcase.server.dto.GenericResponseDto;
import sperka.pl.bookcase.server.exceptions.ValidationException;
import sperka.pl.bookcase.server.service.BookDatabaseSynchronizationService;
import sperka.pl.bookcase.server.service.BookService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path( "/api/books" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
@RolesAllowed( { Roles.ADMIN, Roles.USER } )
public class BooksResource {
    private final BookDatabaseSynchronizationService bookDatabaseSynchronizationService;
    private final BookService bookService;

    @Inject
    public BooksResource( BookDatabaseSynchronizationService bookDatabaseSynchronizationService, BookService bookService ) {
        this.bookDatabaseSynchronizationService = bookDatabaseSynchronizationService;
        this.bookService = bookService;
    }

    @GET
    @Path( "/all" )
    public Response list() {
        return Response.ok().entity( bookService.getAll() ).build();
    }

    @POST
    @Path( "/page/{page}/{perPage}" )
    public Response page( @PathParam( "page" ) int page, @PathParam( "perPage" ) int perPage, BookFilterDto filters ) {
        return Response.ok().entity( bookService.getPaginated( page, perPage, filters, false ) ).build();
    }

    @GET
    @Path( "/count" )
    public Response count() {
        return Response.ok().entity( bookService.getCount() ).build();
    }

    @POST
    @Path( "/deleted/page/{page}/{perPage}" )
    @RolesAllowed( Roles.ADMIN )
    public Response pageDeleted( @PathParam( "page" ) int page, @PathParam( "perPage" ) int perPage, BookFilterDto filters ) {
        return Response.ok().entity( bookService.getPaginated( page, perPage, filters, true ) ).build();
    }

    @POST
    @Path( "/restore/{id}" )
    @RolesAllowed( Roles.ADMIN )
    public Response restore( @PathParam( "id" ) long id ) {
        if ( bookService.restore( id ) ) {
            return Response.ok().entity( new GenericResponseDto( "True" ) ).build();
        } else {
            return Response.status( Response.Status.BAD_REQUEST ).entity( new GenericResponseDto( "False" ) ).build();
        }
    }

    @POST
    @Path( "/deleted/count" )
    @RolesAllowed( Roles.ADMIN )
    public Response countDeleted( BookFilterDto filters ) {
        return Response.ok().entity( bookService.getCount( filters, true ) ).build();
    }

    @POST
    @Path( "/count" )
    public Response count( BookFilterDto filters ) {
        return Response.ok().entity( bookService.getCount( filters, false ) ).build();
    }

    @GET
    @Path( "/id/{id}" )
    public Response byId( @PathParam( "id" ) Long id ) {
        try {
            return Response.ok().entity( bookService.get( id ) ).build();
        } catch ( Exception ex ) {
            return Response.status( Response.Status.NOT_FOUND ).entity( new GenericResponseDto( ex.getMessage() ) ).build();
        }
    }

    @POST
    @Path( "save" )
    public Response save( BookDto book ) {
        try {
            return Response.ok().entity( bookService.save( book ) ).build();
        } catch ( ValidationException ex ) {
            return Response.status( Response.Status.FORBIDDEN ).entity( ex.getViolations() ).build();
        }
    }

    @DELETE
    @Path( "delete/{id}" )
    public Response delete( @PathParam( "id" ) Long id ) {
        try {
            bookService.delete( id );
            return Response.ok().build();
        } catch ( Exception ex ) {
            return Response.status( Response.Status.NOT_FOUND ).entity( new GenericResponseDto( ex.getMessage() ) ).build();
        }
    }

    @POST
    @Path( "/sync" )
    public List< BookDto > syncStart( List< BookDto > books ) {
        return bookDatabaseSynchronizationService.synchronize( books, null ); // TODO
    }
}
