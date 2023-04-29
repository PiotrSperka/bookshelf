package sperka.online.bookcase.server.auth;

import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class SigningKey {
    public static Key getKey() {
        final String key = "E(H+MbQeThWmZq4t7w9z$C&F)J@NcRfU";
        return Keys.hmacShaKeyFor(key.getBytes( StandardCharsets.UTF_8));
    }
}
