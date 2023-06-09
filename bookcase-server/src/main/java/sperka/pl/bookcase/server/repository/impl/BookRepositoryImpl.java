package sperka.pl.bookcase.server.repository.impl;

import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.dto.BookFilterDto;
import sperka.pl.bookcase.server.entity.Book;
import sperka.pl.bookcase.server.repository.BookRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class BookRepositoryImpl extends BasicRepositoryImpl< Book > implements BookRepository {
    public BookRepositoryImpl() {
        super( Book.class );
    }

    @Override
    public List< Book > findSimilar( BookDto book ) {
        return Collections.emptyList();
    }

    @Override
    public List< Book > getCreatedAfter( Instant date ) {
        return entityManager.createQuery( "select b from Book b where b.createDate > :date", Book.class )
                .setParameter( "date", date ).getResultList();
    }

    @Override
    public List< Book > getModifiedAfter( Instant date ) {
        return entityManager.createQuery( "select b from Book b where b.modifyDate > :date", Book.class )
                .setParameter( "date", date ).getResultList();
    }

    @Override
    public Book getLastModified() {
        TypedQuery< Book > query = entityManager
                .createQuery( "select b from Book b order by b.modifyDate desc", Book.class );
        query.setMaxResults( 1 );
        return query.getSingleResult();
    }

    @Override
    public Book getBySignature( String signature ) {
        TypedQuery< Book > query = entityManager
                .createQuery( "select b from Book b where b.signature = :signature and b.deleted = false", Book.class )
                .setParameter( "signature", signature );
        query.setMaxResults( 1 );
        return query.getResultStream().findAny().orElse( null );
    }

    @Override
    public List< Book > getPaginated( int page, int perPage, BookFilterDto filters, boolean deleted ) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery( Book.class );
        var rootEntry = criteriaQuery.from( Book.class );

        criteriaQuery.select( rootEntry );
        Predicate whereCriteria = getFilterPredicate( filters, rootEntry, deleted );

        criteriaQuery.where( whereCriteria );
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

    private Predicate getFilterPredicate( BookFilterDto filters, Root< Book > rootEntry, boolean deleted ) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var whereCriteria = criteriaBuilder.equal( rootEntry.get( "deleted" ), deleted );

        if ( filters != null && filters.getAuthor() != null ) {
            whereCriteria = criteriaBuilder.and( whereCriteria, criteriaBuilder.like( criteriaBuilder.lower( rootEntry.get( "author" ) ), "%" + filters.getAuthor().toLowerCase() + "%" ) );
        }
        if ( filters != null && filters.getTitle() != null ) {
            whereCriteria = criteriaBuilder.and( whereCriteria, criteriaBuilder.like( criteriaBuilder.lower( rootEntry.get( "title" ) ), "%" + filters.getTitle().toLowerCase() + "%" ) );
        }
        if ( filters != null && filters.getRelease() != null ) {
            whereCriteria = criteriaBuilder.and( whereCriteria, criteriaBuilder.like( criteriaBuilder.lower( rootEntry.get( "released" ) ), "%" + filters.getRelease().toLowerCase() + "%" ) );
        }
        if ( filters != null && filters.getSignature() != null ) {
            whereCriteria = criteriaBuilder.and( whereCriteria, criteriaBuilder.like( criteriaBuilder.lower( rootEntry.get( "signature" ) ), "%" + filters.getSignature().toLowerCase() + "%" ) );
        }
        return whereCriteria;
    }

    @Override
    public Long getCountFiltered( BookFilterDto filters, boolean deleted ) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery( Long.class );
        var rootEntry = criteriaQuery.from( Book.class );

        criteriaQuery.select( criteriaBuilder.count( rootEntry ) );
        Predicate whereCriteria = getFilterPredicate( filters, rootEntry, deleted );

        criteriaQuery.where( whereCriteria );

        var query = entityManager.createQuery( criteriaQuery );
        return query.getSingleResult();
    }

    @Override
    public Long getCountNotDeleted() {
        var query = entityManager.createQuery( "select count(b.id) from Book b where b.deleted is false" );
        return ( long ) query.getSingleResult();
    }
}
