package sperka.pl.bookcase.server.auth;

import io.jsonwebtoken.security.Keys;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Singleton
public class SigningKey {
    private final String key;

    @Inject
    public SigningKey( @ConfigProperty( name = "application.password.secret", defaultValue = "" ) String key ) {
        this.key = key;
        if ( key.length() < 32 ) {
            throw new IllegalArgumentException( "application.password.secret needs to be at least 32 characters long" );
        }
    }

    public Key getKey() {
        return Keys.hmacShaKeyFor( key.getBytes( StandardCharsets.UTF_8 ) );
    }
}
