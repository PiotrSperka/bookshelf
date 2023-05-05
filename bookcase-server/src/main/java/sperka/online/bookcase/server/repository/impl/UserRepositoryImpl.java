package sperka.online.bookcase.server.repository.impl;

import sperka.online.bookcase.server.entity.User;
import sperka.online.bookcase.server.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepositoryImpl extends BasicRepositoryImpl< User > implements UserRepository {
    public UserRepositoryImpl() {
        super( User.class );
    }

    @Override
    public User getUserByUsername( String username ) {
        return entityManager.createQuery( "select u from User u where u.name like :name", User.class )
                .setParameter( "name", username ).getSingleResult();
    }

    @Override
    public long countAll() {
        return ( long ) entityManager.createQuery( "select count(u.id) from User u" ).getSingleResult();
    }
}
