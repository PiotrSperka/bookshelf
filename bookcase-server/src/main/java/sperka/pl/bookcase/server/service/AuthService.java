package sperka.pl.bookcase.server.service;

public interface AuthService {
    String loginUser( String username, String password );
    void blacklist( String jwt );
    boolean isBlacklisted( String jwt );
    String refresh( String jwt );
}
