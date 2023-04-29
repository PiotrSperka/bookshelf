package sperka.online.bookcase.server.resource;

import sperka.online.bookcase.server.auth.Roles;
import sperka.online.bookcase.server.dto.CreateUserRequestDto;
import sperka.online.bookcase.server.dto.GenericResponseDto;
import sperka.online.bookcase.server.dto.ModifyUserRequestDto;
import sperka.online.bookcase.server.dto.UserInfoDto;
import sperka.online.bookcase.server.service.AuthService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@ApplicationScoped
@Path( "/user" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class UserResource {
    AuthService authService;

    @Inject
    public UserResource( AuthService authService ) {
        this.authService = authService;
    }

//    @Path( "/coldstart" )
//    @PermitAll
//    @GET
//    public Response coldstart() {
//        var result = authService.createUser( "user", "user", Roles.USER );
//        if ( result ) {
//            return Response.ok( new GenericResponseDto( "OK" ) ).build();
//        }
//
//        return Response.status( Response.Status.BAD_REQUEST ).entity( new GenericResponseDto( "Wrong input" ) ).build();
//    }

    @Path( "/add" )
    @RolesAllowed( Roles.ADMIN )
    @POST
    public Response addUser( CreateUserRequestDto request ) {
        var result = authService.createUser( request.getUsername(), request.getPassword(), String.join( ",", request.getRoles() ) );
        if ( result ) {
            return Response.ok( new GenericResponseDto( "OK" ) ).build();
        }

        return Response.status( Response.Status.BAD_REQUEST ).entity( new GenericResponseDto( "Wrong input" ) ).build();
    }

    @Path( "/edit" )
    @RolesAllowed( Roles.ADMIN )
    @POST
    public Response editUser( ModifyUserRequestDto request ) {
        var result = authService.modifyUser( request.getUsername(), request.getPassword(), request.getRoles() );
        if ( result ) {
            return Response.ok( new GenericResponseDto( "OK" ) ).build();
        }

        return Response.status( Response.Status.BAD_REQUEST ).entity( new GenericResponseDto( "Wrong input" ) ).build();
    }

    @Path( "/delete/{username}" )
    @RolesAllowed( Roles.ADMIN )
    @POST
    public Response addUser( @PathParam( "username" ) String username, @Context SecurityContext securityContext ) {
        var result = authService.deleteUser( username, securityContext.getUserPrincipal().getName() );
        if ( result ) {
            return Response.ok( new GenericResponseDto( "OK" ) ).build();
        }

        return Response.status( Response.Status.BAD_REQUEST ).entity( new GenericResponseDto( "Cannot perform this operation" ) ).build();
    }

    @GET
    @RolesAllowed( { Roles.ADMIN, Roles.USER } )
    @Path( "/info" )
    public UserInfoDto getUserInfo( @Context SecurityContext securityContext ) {
        return new UserInfoDto( securityContext.getUserPrincipal().getName() );
    }
}
