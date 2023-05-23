package sperka.pl.bookcase.server.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JwtSecurityContext implements SecurityContext {
    private final String username;
    private final List< String > roles;

    private final boolean isSecure;

    public JwtSecurityContext( Jws< Claims > jws, SecurityContext currentSecurityContext ) {
        username = jws.getBody().getSubject();
        roles = Arrays.stream( jws.getBody().get( "roles", String.class ).split( "," ) ).collect( Collectors.toList() );
        isSecure = currentSecurityContext.isSecure();
    }

    public JwtSecurityContext( SecurityContext currentSecurityContext ) {
        username = null;
        roles = Collections.emptyList();
        isSecure = currentSecurityContext.isSecure();
    }

    @Override
    public Principal getUserPrincipal() {
        return new JwtUserPrincipal( username, roles );
    }

    @Override
    public boolean isUserInRole( String s ) {
        return roles.stream().anyMatch( role -> role.equals( s ) );
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}
