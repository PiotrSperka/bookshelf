package sperka.pl.bookcase.server.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.RuntimeDelegate;

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
        InputStream inputStream = FrontendResource.class.getResourceAsStream( "/frontend/" + fileName );
        if ( inputStream == null ) {
            inputStream = FrontendResource.class.getResourceAsStream( "/frontend/index.html" );
        }
        if ( inputStream != null ) {
            return Response
                    .ok( inputStream )
                    .cacheControl( RuntimeDelegate.getInstance().createHeaderDelegate( CacheControl.class ).fromString( "max-age=900" ) )
                    .type( URLConnection.guessContentTypeFromStream( inputStream ) )
                    .build();
        }

        return Response.status( Response.Status.NOT_FOUND ).build();
    }
}
