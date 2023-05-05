package sperka.online.bookcase.server.resource;

import lombok.extern.slf4j.Slf4j;
import sperka.online.bookcase.server.auth.Roles;
import sperka.online.bookcase.server.dto.BookDto;
import sperka.online.bookcase.server.dto.BookFilterDto;
import sperka.online.bookcase.server.entity.Book;
import sperka.online.bookcase.server.helpers.InstantParam;
import sperka.online.bookcase.server.repository.BookRepository;
import sperka.online.bookcase.server.service.BookDatabaseSynchronizationService;
import sperka.online.bookcase.server.service.BookService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path( "/books" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
@RolesAllowed( { Roles.ADMIN, Roles.USER } )
@Slf4j
public class BooksResource {
    private final BookRepository bookRepository;
    private final BookDatabaseSynchronizationService bookDatabaseSynchronizationService;
    private final BookService bookService;

    @Inject
    public BooksResource( BookRepository bookRepository, BookDatabaseSynchronizationService bookDatabaseSynchronizationService, BookService bookService ) {
        this.bookRepository = bookRepository;
        this.bookDatabaseSynchronizationService = bookDatabaseSynchronizationService;
        this.bookService = bookService;
    }

    @GET
    @Path( "/all" )
    public List< BookDto > list() {
        return bookRepository.getAll().stream().map( Book::toDto ).collect( Collectors.toList() );
    }

    @POST
    @Path( "/page/{page}/{perPage}" )
    public List< BookDto > page( @PathParam( "page" ) int page, @PathParam( "perPage" ) int perPage, BookFilterDto filters ) {
        return bookRepository.getPaginated( page, perPage, filters ).stream().map( Book::toDto ).collect( Collectors.toList() );
    }

    @GET
    @Path( "/count" )
    public Long count() {
        return bookRepository.getCountNotDeleted();
    }

    @POST
    @Path( "/count" )
    public Long count( BookFilterDto filters ) {
        return bookRepository.getCountFiltered( filters );
    }

    @GET
    @Path( "/id/{id}" )
    public BookDto byId( @PathParam( "id" ) Long id ) {
        Book book = bookRepository.getById( id );
        return ( book != null ) ? book.toDto() : null;
    }

    @POST
    @Path( "save" )
    public Response save( BookDto book ) {
        try {
            return Response.ok().entity( bookService.save( book ) ).build();
        } catch ( ConstraintViolationException ex ) {
            return Response.status( Response.Status.FORBIDDEN ).entity( ex.getConstraintViolations().stream().map( ConstraintViolation::getMessage ).collect( Collectors.toList() ) ).build();
        }
    }

    @DELETE
    @Path( "delete/{id}" )
    public Response delete( @PathParam( "id" ) Long id ) {
        bookService.delete( id );
        return Response.ok().build();
    }

    @GET
    @Path( "/after/{date}" )
    public List< BookDto > afterDate( @PathParam( "date" ) InstantParam date ) {
        return bookRepository.getCreatedAfter( date.getInstant() ).stream().map( Book::toDto ).collect( Collectors.toList() );
    }

    @GET
    @Path( "/last" )
    public BookDto last() {
        Book book = bookRepository.getLastModified();
        return ( book != null ) ? book.toDto() : null;
    }

    @POST
    @Path( "/sync" )
    public List< BookDto > syncStart( List< BookDto > books ) {
        return bookDatabaseSynchronizationService.synchronize( books, null ); // TODO
    }
}
