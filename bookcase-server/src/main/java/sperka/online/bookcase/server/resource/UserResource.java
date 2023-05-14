package sperka.online.bookcase.server.resource;

import sperka.online.bookcase.server.auth.Roles;
import sperka.online.bookcase.server.dto.*;
import sperka.online.bookcase.server.service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@ApplicationScoped
@Path( "/api/user" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class UserResource {
    UserService userService;

    @Inject
    public UserResource( UserService userService ) {
        this.userService = userService;
    }

    @Path( "/all" )
    @RolesAllowed( Roles.ADMIN )
    @GET
    public Response getAll() {
        return Response.ok().entity( userService.getAll() ).build();
    }

    @Path( "/id/{id}" )
    @RolesAllowed( Roles.ADMIN )
    @GET
    public Response get( @PathParam( "id" ) Long id ) {
        var user = userService.get( id );
        if ( user != null ) {
            return Response.ok().entity( user ).build();
        }

        return Response.status( Response.Status.NOT_FOUND ).entity( new GenericResponseDto( "User not found" ) ).build();
    }

    @Path( "/add" )
    @RolesAllowed( Roles.ADMIN )
    @POST
    public Response addUser( CreateUserRequestDto request ) {
        var result = userService.createUser( request.getName(), request.getPassword(), request.getRoles() );
        if ( result ) {
            return Response.ok( new GenericResponseDto( "OK" ) ).build();
        }

        return Response.status( Response.Status.BAD_REQUEST ).entity( new GenericResponseDto( "Wrong input" ) ).build();
    }

    @Path( "/edit" )
    @RolesAllowed( Roles.ADMIN )
    @POST
    public Response editUser( ModifyUserRequestDto request ) {
        var result = userService.modifyUser( request.getId(), request.getName(), request.getPassword(), request.getRoles(), request.getActive() );
        if ( result ) {
            return Response.ok( new GenericResponseDto( "OK" ) ).build();
        }

        return Response.status( Response.Status.BAD_REQUEST ).entity( new GenericResponseDto( "Wrong input" ) ).build();
    }

    @Path( "/id/{id}" )
    @RolesAllowed( Roles.ADMIN )
    @DELETE
    public Response addUser( @PathParam( "id" ) Long id, @Context SecurityContext securityContext ) {
        var result = userService.deleteUser( id, securityContext.getUserPrincipal().getName() );
        if ( result ) {
            return Response.ok( new GenericResponseDto( "OK" ) ).build();
        }

        return Response.status( Response.Status.BAD_REQUEST ).entity( new GenericResponseDto( "Cannot perform this operation" ) ).build();
    }

    @POST
    @RolesAllowed( { Roles.ADMIN, Roles.USER } )
    @Path( "/change-password" )
    public Response changePassword( ChangePasswordDto dto, @Context SecurityContext securityContext ) {
        if ( !dto.getNewPassword().equals( dto.getNewPasswordRepeat() ) ) {
            return Response.status( Response.Status.FORBIDDEN ).entity( new GenericResponseDto( "New password does not match" ) ).build();
        }

        if ( userService.modifyPassword( securityContext.getUserPrincipal().getName(), dto.getCurrentPassword(), dto.getNewPassword() ) ) {
            return Response.ok().entity( new GenericResponseDto( "OK" ) ).build();
        }

        return Response.status( Response.Status.BAD_REQUEST ).build();
    }

    @GET
    @RolesAllowed( { Roles.ADMIN, Roles.USER } )
    @Path( "/info" )
    public UserInfoDto getUserInfo( @Context SecurityContext securityContext ) {
        var principal = securityContext.getUserPrincipal();
        return userService.getByUsername( principal.getName() );
    }
}
