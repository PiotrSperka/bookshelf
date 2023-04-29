package sperka.online.bookcase.server.resource;

import lombok.extern.slf4j.Slf4j;
import sperka.online.bookcase.server.dto.LoginRequestDto;
import sperka.online.bookcase.server.dto.LoginResponseDto;
import sperka.online.bookcase.server.service.AuthService;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    public LoginResponseDto login( LoginRequestDto dto ) {
        var token = authService.loginUser( dto.getUsername(), dto.getPassword() );
        if ( token != null ) {
            return new LoginResponseDto( token );
        }

        throw new NotFoundException( "Wrong username or password" );
    }

    @Path( "test" )
    @PermitAll
    @GET
    public String test() {
        authService.createUser( "user", "user", "user" );
        authService.createUser( "admin", "admin", "admin" );

        return "OK";
    }
}
