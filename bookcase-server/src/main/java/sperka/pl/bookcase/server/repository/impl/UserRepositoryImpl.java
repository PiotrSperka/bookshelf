package sperka.pl.bookcase.server.repository.impl;

import jakarta.enterprise.context.ApplicationScoped;
import sperka.pl.bookcase.server.entity.User;
import sperka.pl.bookcase.server.repository.UserRepository;

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
    public User getUserByResetPasswordToken( String resetToken ) {
        return entityManager.createQuery( "select u from User u where u.resetPasswordToken = :token", User.class )
                .setParameter( "token", resetToken ).getResultStream().findFirst().orElse( null );
    }

    @Override
    public long countAll() {
        return ( long ) entityManager.createQuery( "select count(u.id) from User u" ).getSingleResult();
    }
}
