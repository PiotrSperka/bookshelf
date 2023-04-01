package sperka.online.bookcase.server.repository.impl;

import sperka.online.bookcase.commons.dto.BookDto;
import sperka.online.bookcase.server.entity.Book;
import sperka.online.bookcase.server.repository.BookRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class BookRepositoryImpl extends BasicRepositoryImpl<Book> implements BookRepository {
    public BookRepositoryImpl() {
        super(Book.class);
    }

    @Override
    public List<Book> findSimilar(BookDto book) {
        return Collections.emptyList();
    }

    @Override
    public List<Book> getCreatedAfter(Instant date) {
        return entityManager.createQuery("select b from Book b where b.createDate > :date", Book.class)
                .setParameter("date", date).getResultList();
    }

    @Override
    public List<Book> getModifiedAfter(Instant date) {
        return entityManager.createQuery("select b from Book b where b.modifyDate > :date", Book.class)
                .setParameter("date", date).getResultList();
    }

    @Override
    public Book getLastModified() {
        TypedQuery<Book> query = entityManager
                .createQuery("select b from Book b order by b.modifyDate desc", Book.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }
}
