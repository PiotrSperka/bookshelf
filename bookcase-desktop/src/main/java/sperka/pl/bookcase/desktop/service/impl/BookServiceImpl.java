package sperka.pl.bookcase.desktop.service.impl;

import org.springframework.stereotype.Service;
import sperka.pl.bookcase.desktop.entity.Book;
import sperka.pl.bookcase.desktop.repository.BookRepository;
import sperka.pl.bookcase.desktop.service.BookService;

import java.time.Instant;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl( BookRepository bookRepository ) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List< Book > getAll() {
        return bookRepository.deletedIsFalse();
    }

    @Override
    public Book get( Long id ) {
        return bookRepository.getOne( id );
    }

    @Override
    public Book getByRemoteId( Long remoteId ) {
        return bookRepository.findByRemoteId( remoteId );
    }

    @Override
    public List< Book > getModifiedAfter( Instant modificationDate ) {
        return bookRepository.findModifiedAfter( modificationDate );
    }

    @Override
    public boolean exists( Book book ) {
        List< Book > books = bookRepository.findBySignatureAndDeletedIsFalse( book.getSignature() );
        return !books.isEmpty();
    }

    @Override
    public void save( Book book ) {
        bookRepository.save( book );
    }
}
