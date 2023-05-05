package sperka.online.bookcase.server.resource;

import lombok.extern.slf4j.Slf4j;
import sperka.online.bookcase.server.dto.GenericResponseDto;
import sperka.online.bookcase.server.dto.LoginRequestDto;
import sperka.online.bookcase.server.dto.LoginResponseDto;
import sperka.online.bookcase.server.service.AuthService;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path( "/auth" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
@Slf4j
public class AuthResource {
    AuthService authService;

    @Inject
    public AuthResource( AuthService authService ) {
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

    @Path( "init" )
    @PermitAll
    @GET
    public Response initUsers() {
        if ( authService.initializeUsers() ) {
            return Response.ok().build();
        } else {
            return Response.status( Response.Status.FORBIDDEN ).build();
        }
    }
}
