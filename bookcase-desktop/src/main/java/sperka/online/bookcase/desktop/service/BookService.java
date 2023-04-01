package sperka.online.bookcase.desktop.service;

import sperka.online.bookcase.desktop.entity.Book;

import java.time.Instant;
import java.util.List;

public interface BookService {
    List< Book > getAll();

    Book get( Long id );

    Book getByRemoteId( Long remoteId );

    List< Book > getModifiedAfter( Instant modificationDate );

    boolean exists( Book book );

    void save( Book book );
}
