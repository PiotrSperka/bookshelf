package sperka.pl.bookcase.server.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@ApplicationScoped
@Path( "/" )
public class FrontendResource {
    @GET
    @Path( "/" )
    public Response getFrontendRoot() throws IOException {
        return getFrontendStaticFile( "index.html" );
    }

    @GET
    @Path( "/{fileName:.+}" )
    public Response getFrontendStaticFile( @PathParam( "fileName" ) String fileName ) throws IOException {
        final InputStream inputStream = FrontendResource.class.getResourceAsStream( "/frontend/" + fileName );
        if ( inputStream != null ) {
            return Response
                    .ok( inputStream )
                    .cacheControl( CacheControl.valueOf( "max-age=900" ) )
                    .type( URLConnection.guessContentTypeFromStream( inputStream ) )
                    .build();
        }

        return Response.status( Response.Status.NOT_FOUND ).build();
    }
}
