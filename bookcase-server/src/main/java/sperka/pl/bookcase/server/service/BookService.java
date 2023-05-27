package sperka.pl.bookcase.server.service;

import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.dto.BookFilterDto;

import java.util.List;

public interface BookService {
    List< BookDto > getAll();

    List< BookDto > getPaginated( int page, int perPage, BookFilterDto filters, boolean deleted );

    Long getCount( BookFilterDto filters, boolean deleted );

    Long getCount();

    BookDto get( Long id );

    BookDto save( BookDto dto );

    void delete( Long id );

    boolean restore( Long id );
}
