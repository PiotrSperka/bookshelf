package sperka.pl.bookcase.server.repository;

import sperka.pl.bookcase.server.entity.JwtBlacklist;

import java.util.List;

public interface JwtBlacklistRepository extends BasicRepository< JwtBlacklist > {
    List< JwtBlacklist > findByToken( String token );

    Long countByToken( String token );
}
