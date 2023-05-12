package sperka.online.bookcase.server.repository;

import sperka.online.bookcase.server.entity.JwtBlacklist;

import java.util.List;

public interface JwtBlacklistRepository extends BasicRepository< JwtBlacklist > {
    List< JwtBlacklist > findByToken( String token );

    Long countByToken( String token );
}
