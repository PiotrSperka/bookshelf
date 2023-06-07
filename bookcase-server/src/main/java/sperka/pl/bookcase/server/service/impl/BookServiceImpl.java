package sperka.pl.bookcase.server.service.impl;

import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.dto.BookFilterDto;
import sperka.pl.bookcase.server.entity.Book;
import sperka.pl.bookcase.server.exceptions.ValidationException;
import sperka.pl.bookcase.server.repository.BookRepository;
import sperka.pl.bookcase.server.service.BookService;
import sperka.pl.bookcase.server.service.LogService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final LogService logService;
    private final Validator validator;

    public BookServiceImpl( BookRepository bookRepository, LogService logService, Validator validator ) {
        this.bookRepository = bookRepository;
        this.logService = logService;
        this.validator = validator;
    }

    @Override
    public List< BookDto > getAll() {
        return bookRepository.getAll().stream().map( Book::toDto ).collect( Collectors.toList() );
    }

    @Override
    public List< BookDto > getPaginated( int page, int perPage, BookFilterDto filters, boolean deleted ) {
        return bookRepository.getPaginated( page, perPage, filters, deleted ).stream().map( Book::toDto ).collect( Collectors.toList() );
    }

    @Override
    public Long getCount( BookFilterDto filters, boolean deleted ) {
        return bookRepository.getCountFiltered( filters, deleted );
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
    public BookDto save( BookDto dto ) {
        var validationException = new ValidationException();

        for ( var violation : validator.validate( dto ) ) {
            if ( violation.getMessage().equals( "books.error.signature-exists" ) ) {
                validationException.addViolation( "signature", violation.getMessage() );
            } else {
                validationException.addViolation( violation.getPropertyPath().toString(), violation.getMessage() );
            }
        }

        if ( !validationException.isEmpty() ) {
            throw validationException;
        }

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

    @Override
    @Transactional
    public boolean restore( Long id ) {
        var book = bookRepository.getById( id );
        if ( book != null && book.getDeleted() ) {
            book.setDeleted( false );
            book.setModifyDate( Instant.now() );
            book = bookRepository.save( book );

            logService.add( "Restored " + book.toString(), "system" );

            return true;
        }

        return false;
    }
}
