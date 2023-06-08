package sperka.pl.bookcase.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.Logger;
import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.entity.Book;
import sperka.pl.bookcase.server.repository.BookRepository;
import sperka.pl.bookcase.server.service.BookDatabaseSynchronizationService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class BookDatabaseSynchronizationServiceImpl implements BookDatabaseSynchronizationService {
    private final BookRepository bookRepository;

    @Inject
    public BookDatabaseSynchronizationServiceImpl( BookRepository bookRepository ) {
        this.bookRepository = bookRepository;
    }

    // TODO: much more efficient way
    private List< Book > findSimilar( BookDto book ) {
        var books = bookRepository.getAll();

        if ( book.getRemoteId() != null ) {
            return books.stream()
                    .filter( b -> b.getId().equals( book.getRemoteId() ) )
                    .collect( Collectors.toList() );
        } else {
            return books.stream()
                    .filter( b -> b.getAuthor().equals( book.getAuthor() ) )
                    .filter( b -> b.getTitle().equals( book.getTitle() ) )
                    .filter( b -> b.getSignature().equals( book.getSignature() ) )
                    .collect( Collectors.toList() );
        }
    }

    @Override
    @Transactional
    public List< BookDto > synchronize( List< BookDto > books, Instant lastSyncTime ) {
        List< BookDto > modifiedBooks = new ArrayList<>();

        if ( books == null ) {
            return modifiedBooks;
        }

        books.forEach( book -> {
            List< Book > similarBooks = findSimilar( book );
            if ( similarBooks.isEmpty() ) {
                Book entity = new Book();
                entity.fromDto( book );
                var savedBook = bookRepository.save( entity );
                book.setRemoteId( savedBook.getId() );
                modifiedBooks.add( book );
            } else if ( similarBooks.size() == 1 ) {
                Book entity = similarBooks.get( 0 );

                if ( entity.getModifyDate().isBefore( book.getModifyDate() ) ) {
                    entity.fromDto( book );
                    var savedBook = bookRepository.save( entity );
                    book.setRemoteId( savedBook.getId() );
                    modifiedBooks.add( book );
                } else {
                    var dto = entity.toDto();
                    dto.setLocalId( book.getLocalId() );
                    modifiedBooks.add( dto );
                }
            } else {
                log.warn( "Found more than one similar book: " + similarBooks );
                // TODO:
            }
        } );

        // Add all books modified after last modification date of client
        var modifiedAfterSync = bookRepository.getModifiedAfter( lastSyncTime );
        modifiedAfterSync.forEach( book -> {
            if ( books.stream().noneMatch( b -> book.getId().equals( b.getRemoteId() ) ) ) {
                modifiedBooks.add( book.toDto() );
            }
        } );

        return modifiedBooks;
    }
}
