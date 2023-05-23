package sperka.pl.bookcase.server.service;

import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.dto.BookFilterDto;

import javax.validation.Valid;
import java.util.List;

public interface BookService {
    List< BookDto > getAll();

    List< BookDto > getPaginated( int page, int perPage, BookFilterDto filters );

    Long getCount( BookFilterDto filters );

    Long getCount();

    BookDto get( Long id );

    BookDto save( @Valid BookDto dto );

    void delete( Long id );
}
