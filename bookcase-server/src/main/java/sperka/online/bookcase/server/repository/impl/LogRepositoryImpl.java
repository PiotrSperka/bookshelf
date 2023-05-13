package sperka.online.bookcase.server.repository.impl;

import sperka.online.bookcase.server.dto.LogFilterDto;
import sperka.online.bookcase.server.entity.Log;
import sperka.online.bookcase.server.repository.LogRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
//        Predicate whereCriteria = getFilterPredicate( filters, rootEntry );

//        criteriaQuery.where( whereCriteria );
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
//        Predicate whereCriteria = getFilterPredicate( filters, rootEntry );

//        criteriaQuery.where( whereCriteria );

        var query = entityManager.createQuery( criteriaQuery );
        return query.getSingleResult();
    }

    private Predicate getFilterPredicate( LogFilterDto filters, Root< Log > rootEntry ) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var whereCriteria = criteriaBuilder.ge( rootEntry.get( "id" ), 0 );

        return whereCriteria;
    }

}
