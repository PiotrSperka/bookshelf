package sperka.pl.bookcase.server;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class ServerTestProfile implements QuarkusTestProfile {
    public Map< String, String > getConfigOverrides() {
        return Map.of( "application.password.secret", "abcdefghijklmnoprstuwvxyz0123456" );
    }
}
