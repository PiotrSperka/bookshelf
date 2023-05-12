package sperka.online.bookcase.server.repository.impl;

import sperka.online.bookcase.server.entity.JwtBlacklist;
import sperka.online.bookcase.server.repository.JwtBlacklistRepository;

import javax.enterprise.context.ApplicationScoped;
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
