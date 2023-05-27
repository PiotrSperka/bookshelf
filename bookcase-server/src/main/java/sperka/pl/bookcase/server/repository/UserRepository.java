package sperka.pl.bookcase.server.repository;

import sperka.pl.bookcase.server.entity.User;

public interface UserRepository extends BasicRepository< User > {
    User getUserByUsername( String username );
    long countAll();
}
