package sperka.pl.bookcase.server.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.Principal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserPrincipal implements Principal {
    private String name;
    private List<String> roles;
}
