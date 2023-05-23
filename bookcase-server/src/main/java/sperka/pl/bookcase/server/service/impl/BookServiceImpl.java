package sperka.pl.bookcase.server.service.impl;

import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.dto.BookFilterDto;
import sperka.pl.bookcase.server.entity.Book;
import sperka.pl.bookcase.server.repository.BookRepository;
import sperka.pl.bookcase.server.service.BookService;
import sperka.pl.bookcase.server.service.LogService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final LogService logService;

    public BookServiceImpl( BookRepository bookRepository, LogService logService ) {
        this.bookRepository = bookRepository;
        this.logService = logService;
    }

    @Override
    public List< BookDto > getAll() {
        return bookRepository.getAll().stream().map( Book::toDto ).collect( Collectors.toList() );
    }

    @Override
    public List< BookDto > getPaginated( int page, int perPage, BookFilterDto filters ) {
        return bookRepository.getPaginated( page, perPage, filters ).stream().map( Book::toDto ).collect( Collectors.toList() );
    }

    @Override
    public Long getCount( BookFilterDto filters ) {
        return bookRepository.getCountFiltered( filters );
    }

    @Override
    public Long getCount() {
        return bookRepository.getCountNotDeleted();
    }

    @Override
    public BookDto get( Long id ) {
        return bookRepository.getById( id ).toDto();
    }

    @Override
    @Transactional
    public BookDto save( @Valid BookDto dto ) {
        Book entity;
        if ( dto.getRemoteId() != null ) {
            entity = bookRepository.getById( dto.getRemoteId() );
            entity.setAuthor( dto.getAuthor() );
            entity.setTitle( dto.getTitle() );
            entity.setReleased( dto.getReleased() );
            entity.setSignature( dto.getSignature() );
        } else {
            entity = new Book();
            entity.fromDto( dto );
            entity.setDeleted( false );
            entity.setCreateDate( Instant.now() );
        }

        entity.setModifyDate( Instant.now() );
        entity = bookRepository.save( entity );

        logService.add( "Saved book " + entity.toString(), "system" );

        return entity.toDto();
    }

    @Override
    @Transactional
    public void delete( Long id ) {
        bookRepository.delete( id );
        logService.add( "Deleted book id = " + id, "system" );
    }
}
