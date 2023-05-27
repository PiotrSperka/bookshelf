package sperka.pl.bookcase.server.repository.impl;

import sperka.pl.bookcase.server.entity.IdProvider;
import sperka.pl.bookcase.server.repository.BasicRepository;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class BasicRepositoryImpl<T extends IdProvider > implements BasicRepository<T> {
    private final Class<T> cls;
    @Inject
    protected EntityManager entityManager;

    protected BasicRepositoryImpl(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public T getById(Long id) {
        return entityManager.find(cls, id);
    }

    @Override
    public List<T> getAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(cls);
        Root<T> rootEntry = criteriaQuery.from(cls);
        CriteriaQuery<T> all = criteriaQuery.select(rootEntry);
        TypedQuery<T> allQuery = entityManager.createQuery(all);

        return allQuery.getResultList();
    }

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return entity;
    }

    @Override
    public void delete(T entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    @Override
    public void delete(Long id) {
        T entity = getById(id);
        delete(entity);
    }
}
