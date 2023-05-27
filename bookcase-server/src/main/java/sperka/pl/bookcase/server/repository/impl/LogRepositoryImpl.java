package sperka.pl.bookcase.server.repository.impl;

import jakarta.enterprise.context.ApplicationScoped;
import sperka.pl.bookcase.server.dto.LogFilterDto;
import sperka.pl.bookcase.server.entity.Log;
import sperka.pl.bookcase.server.repository.LogRepository;

import java.util.List;

@ApplicationScoped
public class LogRepositoryImpl extends BasicRepositoryImpl< Log > implements LogRepository {
    public LogRepositoryImpl() {
        super( Log.class );
    }

    @Override
    public List< Log > getPaginated( int page, int perPage, LogFilterDto filters ) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery( Log.class );
        var rootEntry = criteriaQuery.from( Log.class );

        criteriaQuery.select( rootEntry );

        if ( filters.getSortField() != null && !filters.getSortField().isEmpty() ) {
            if ( "desc".equals( filters.getSortDirection() ) ) {
                criteriaQuery.orderBy( criteriaBuilder.desc( rootEntry.get( filters.getSortField() ) ) );
            } else {
                criteriaQuery.orderBy( criteriaBuilder.asc( rootEntry.get( filters.getSortField() ) ) );
            }
        }

        var query = entityManager.createQuery( criteriaQuery );
        query.setMaxResults( perPage );
        query.setFirstResult( ( page - 1 ) * perPage );

        return query.getResultList();
    }

    @Override
    public Long getCount( LogFilterDto filters ) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery( Long.class );
        var rootEntry = criteriaQuery.from( Log.class );

        criteriaQuery.select( criteriaBuilder.count( rootEntry ) );

        var query = entityManager.createQuery( criteriaQuery );
        return query.getSingleResult();
    }

}
