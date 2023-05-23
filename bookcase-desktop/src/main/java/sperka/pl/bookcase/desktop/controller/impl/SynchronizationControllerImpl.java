package sperka.pl.bookcase.desktop.controller.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import sperka.pl.bookcase.commons.dto.BookDto;
import sperka.pl.bookcase.desktop.client.SynchronizationClient;
import sperka.pl.bookcase.desktop.controller.SynchronizationController;
import sperka.pl.bookcase.desktop.entity.Book;
import sperka.pl.bookcase.desktop.entity.Property;
import sperka.pl.bookcase.desktop.service.BookService;
import sperka.pl.bookcase.desktop.service.PropertyService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Controller
public class SynchronizationControllerImpl implements SynchronizationController {
    private final Logger logger = LoggerFactory.getLogger( SynchronizationControllerImpl.class );
    private final BookService bookService;
    private final PropertyService propertyService;
    private final SynchronizationClient synchronizationClient;

    public SynchronizationControllerImpl( BookService bookService, PropertyService propertyService, SynchronizationClient synchronizationClient ) {
        this.bookService = bookService;
        this.propertyService = propertyService;
        this.synchronizationClient = synchronizationClient;
    }

    @Override
    public void synchronize() {
        LocalDateTime synchronizationStartDateTime = LocalDateTime.now();
        LocalDateTime lastSynchronizationDateTime = LocalDateTime.of( 0, 1, 1, 0, 0 );
        Property lastSynchronizationProperty = propertyService.getProperty( "lastSynchronization" );
        if ( lastSynchronizationProperty != null ) {
            lastSynchronizationDateTime = lastSynchronizationProperty.toLocalDateTime();
        }

        try {
            sendCreatedBooksToServer( lastSynchronizationDateTime.toInstant( ZoneOffset.UTC ) );
            propertyService.setProperty( "lastSynchronization", synchronizationStartDateTime.toString() );
        } catch ( Exception ex ) {
            logger.error( ex.getLocalizedMessage(), ex );
        }
    }

    private void sendCreatedBooksToServer( Instant lastSyncTime ) {
        var books = bookService.getModifiedAfter( lastSyncTime );
        var synced = synchronizationClient.synchronize( books.stream().map( Book::toDto ).toList(), lastSyncTime );
        synced.forEach( b -> {
            logger.info( b.toString() );
            findLocalId( b );
            var editedBook = Book.fromDto( b );
            bookService.save( editedBook );
        } );
    }

    private void findLocalId( BookDto b ) {
        if ( b.getLocalId() == null && b.getRemoteId() != null ) {
            var book = bookService.getByRemoteId( b.getRemoteId() );
            if ( book != null ) {
                b.setLocalId( book.getId() );
            }
        }
    }
}
