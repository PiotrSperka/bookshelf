package sperka.online.bookcase.server.repository;

import sperka.online.bookcase.server.entity.User;

public interface UserRepository extends BasicRepository< User > {
    User getUserByUsername( String username );
}
