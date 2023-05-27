package sperka.pl.bookcase.server.resource;

import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import sperka.pl.bookcase.server.auth.JwtUserPrincipal;
import sperka.pl.bookcase.server.dto.GenericResponseDto;
import sperka.pl.bookcase.server.dto.LoginRequestDto;
import sperka.pl.bookcase.server.dto.LoginResponseDto;
import sperka.pl.bookcase.server.service.AuthService;
import sperka.pl.bookcase.server.service.UserService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@ApplicationScoped
@Path( "/api/auth" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
@Slf4j
public class AuthResource {
    UserService userService;
    AuthService authService;

    @Inject
    public AuthResource( UserService userService, AuthService authService ) {
        this.userService = userService;
        this.authService = authService;
    }

    @Path( "login" )
    @PermitAll
    @POST
    public Response login( LoginRequestDto dto ) {
        var token = authService.loginUser( dto.getUsername(), dto.getPassword() );
        if ( token != null ) {
            return Response.ok().entity( new LoginResponseDto( token ) ).build();
        }

        return Response.status( Response.Status.NOT_FOUND ).entity( new GenericResponseDto( "Wrong username or password" ) ).build();
    }

    @Path( "logout" )
    @PermitAll
    @GET
    public Response logout( @HeaderParam( "Authorization" ) String authorizationHeader, @Context SecurityContext securityContext ) {
        if ( securityContext.getUserPrincipal() instanceof JwtUserPrincipal ) {
            authService.blacklist( authorizationHeader.substring( 7 ) );
        }

        return Response.ok( new GenericResponseDto( "Logged out" ) ).build();
    }

    @Path( "refresh" )
    @PermitAll
    @GET
    public Response refresh( @HeaderParam( "Authorization" ) String authorizationHeader, @Context SecurityContext securityContext ) {
        if ( securityContext.getUserPrincipal() instanceof JwtUserPrincipal ) {
            var newToken = authService.refresh( authorizationHeader.substring( 7 ) );
            if ( newToken != null ) {
                return Response.ok().entity( new LoginResponseDto( newToken ) ).build();
            }
        }

        return Response.status( Response.Status.UNAUTHORIZED ).entity( new GenericResponseDto( "Cannot refresh token" ) ).build();
    }

    @Path( "init" )
    @PermitAll
    @GET
    public Response initUsers() {
        if ( userService.initializeUsers() ) {
            return Response.ok().build();
        } else {
            return Response.status( Response.Status.FORBIDDEN ).build();
        }
    }
}
