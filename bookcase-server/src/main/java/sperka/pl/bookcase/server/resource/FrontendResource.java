package sperka.pl.bookcase.server.resource;

import org.h2.util.IOUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@ApplicationScoped
@Path( "/" )
public class FrontendResource {
    private static final String FALLBACK_RESOURCE = "/frontend/index.html";

    @GET
    @Path( "/" )
    public Response getFrontendRoot() throws IOException {
        return getFrontendStaticFile( "index.html" );
    }

    @GET
    @Path( "/{fileName:.+}" )
    public Response getFrontendStaticFile( @PathParam( "fileName" ) String fileName ) throws IOException {
        final InputStream requestedFileStream = FrontendResource.class.getResourceAsStream( "/frontend/" + fileName );
        final InputStream inputStream = requestedFileStream != null ?
                requestedFileStream :
                FrontendResource.class.getResourceAsStream( FALLBACK_RESOURCE );
        final StreamingOutput streamingOutput = outputStream -> IOUtils.copy( inputStream, outputStream );
        if ( inputStream != null ) {
            return Response
                    .ok( streamingOutput )
                    .cacheControl( CacheControl.valueOf( "max-age=900" ) )
                    .type( URLConnection.guessContentTypeFromStream( inputStream ) )
                    .build();
        }

        return Response.status( Response.Status.NOT_FOUND ).build();
    }
}
