package sperka.pl.bookcase.server.repository;

import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.dto.BookFilterDto;
import sperka.pl.bookcase.server.entity.Book;

import java.time.Instant;
import java.util.List;

public interface BookRepository extends BasicRepository< Book > {
    List< Book > findSimilar( BookDto book );

    List< Book > getCreatedAfter( Instant date );

    List< Book > getModifiedAfter( Instant date );

    Book getLastModified();

    Book getBySignature( String signature );

    List< Book > getPaginated( int page, int perPage, BookFilterDto filters, boolean deleted );

    Long getCountFiltered( BookFilterDto filters, boolean deleted );

    Long getCountNotDeleted();
}
