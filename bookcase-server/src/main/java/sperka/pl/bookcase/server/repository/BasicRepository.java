package sperka.pl.bookcase.server.repository;

import java.util.List;

public interface BasicRepository<T> {
    T getById(Long id);

    List<T> getAll();

    T save(T entity);

    void delete(T entity);

    void delete(Long id);
}
