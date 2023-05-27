package sperka.pl.bookcase.server.repository.impl;

import sperka.pl.bookcase.server.entity.JwtBlacklist;
import sperka.pl.bookcase.server.repository.JwtBlacklistRepository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class JwtBlacklistRepositoryImpl extends BasicRepositoryImpl< JwtBlacklist > implements JwtBlacklistRepository {
    protected JwtBlacklistRepositoryImpl() {
        super( JwtBlacklist.class );
    }

    @Override
    public List< JwtBlacklist > findByToken( String token ) {
        return entityManager.createQuery( "select j from JwtBlacklist j where j.jwt = :token", JwtBlacklist.class )
                .setParameter( "token", token ).getResultList();
    }

    @Override
    public Long countByToken( String token ) {
        return entityManager.createQuery( "select count(j.id) from JwtBlacklist j where j.jwt = :token", Long.class )
                .setParameter( "token", token ).getSingleResult();
    }
}
