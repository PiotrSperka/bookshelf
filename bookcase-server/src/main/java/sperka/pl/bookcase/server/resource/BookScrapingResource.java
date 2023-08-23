package sperka.pl.bookcase.server.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sperka.pl.bookcase.server.auth.Roles;
import sperka.pl.bookcase.server.dto.BookScrapingJobDto;
import sperka.pl.bookcase.server.dto.GenericResponseDto;
import sperka.pl.bookcase.server.entity.BookScrapingJob;
import sperka.pl.bookcase.server.service.BookScrapingService;

import java.io.IOException;
import java.net.URLConnection;

@ApplicationScoped
@Path( "/api/bookscraping" )
@Consumes( MediaType.APPLICATION_JSON )
@RolesAllowed( { Roles.ADMIN, Roles.USER } )
public class BookScrapingResource {

    private final BookScrapingService bookScrapingService;

    @Inject
    public BookScrapingResource( BookScrapingService bookScrapingService ) {
        this.bookScrapingService = bookScrapingService;
    }

    @GET
    @Path( "/job" )
    public Response listJobs() {
        return Response.ok().entity( bookScrapingService.getAll().stream().map( BookScrapingJob::toDto ).toList() ).build();
    }

    @POST
    @Path( "/job" )
    public Response addJob( BookScrapingJobDto dto ) {
        if ( bookScrapingService.createJob( dto ) ) {
            return Response.ok().entity( new GenericResponseDto( "OK" ) ).build();
        }

        return Response.status( Response.Status.BAD_REQUEST ).build();
    }

    @GET
    @Path( "/result/{id}" )
    public Response getResultFile( @PathParam( "id" ) Long id ) throws IOException {
        if ( id != null ) {
            var resultStream = bookScrapingService.getFile( id );
            return Response.ok( resultStream )
                    .type( URLConnection.guessContentTypeFromStream( resultStream ) )
                    .build();
        }

        return Response.status( Response.Status.BAD_REQUEST ).build();
    }
}
