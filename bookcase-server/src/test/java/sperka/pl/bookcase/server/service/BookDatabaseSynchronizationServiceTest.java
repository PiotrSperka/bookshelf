package sperka.pl.bookcase.server.service;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.entity.Book;
import sperka.pl.bookcase.server.repository.BookRepository;

import javax.inject.Inject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@QuarkusTest
public class BookDatabaseSynchronizationServiceTest {
    @Inject
    BookDatabaseSynchronizationService bookDatabaseSynchronizationService;

    @InjectMock
    BookRepository bookRepository;

    private List< Book > GetBookDb() {
        var books = new ArrayList< Book >();

        books.add( new Book( 1L, "Author One", "Title one", "Berlin, 2022", "A/II/34", Instant.now(), Instant.now(), false ) );
        books.add( new Book( 2L, "Author One", "Title two", "Berlin, 2012", "A/II/35", Instant.now(), Instant.now().plusSeconds( 3600 ), false ) );
        books.add( new Book( 3L, "Author Two", "Title three", "Warsaw, 1922", "A/II/36", Instant.now(), Instant.now(), false ) );
        books.add( new Book( 4L, "Author Three", "Title five", "Rome, 1999", "B/III/14", Instant.now(), Instant.now(), false ) );
        books.add( new Book( 5L, "Author Two", "Title four", "London, 2001", "A/I/3", Instant.now(), Instant.now().minusSeconds( 3600 ), false ) );
        books.add( new Book( 6L, "Author Four", "Title six", "Brno, 2012", "C/II/35", Instant.now(), Instant.now().plusSeconds( 1200 ), false ) );

        return books;
    }

    private List< BookDto > GetDesktopBooks() {
        var books = new ArrayList< BookDto >();

        books.add( new BookDto( 24L, null, "Author One", "Title one", "Berlin, 2022", "A/II/34", Instant.now(), Instant.now(), false ) );
        books.add( new BookDto( 26L, 2L, "Author One", "Title two edit", "Berlin, 2012", "A/II/35", Instant.now(), Instant.now(), false ) );
        books.add( new BookDto( 22L, 5L, "Author Two", "Title four edit", "London, 2001", "A/I/3", Instant.now(), Instant.now(), false ) );
        books.add( new BookDto( 21L, null, "Author Four", "Title one", "Cracow, 2008", "D/II/34", Instant.now(), Instant.now(), false ) );

        return books;
    }

    /*
     * 1. Desktop sends list of all books modified after its last sync date
     * 2. Server iterates books:
     *    - Adds (or checks for existing same book) book and sets its ID into DTO
     *    - Modifies book if incoming has newer modification date (also deleted flag), sets mod. date
     *    - Gets all books with mod date after last desktop sync date and sends them to desktop
     * 3. Desktop gets DTO list:
     *    - When there is localId, it modifies entry to match server
     *    - If not, it searches by remoteId, and modifies to match server entry if found
     *    - If entry was not found, it adds new entry
     * */

    @BeforeEach
    void beforeTests() {
        var booksDb = GetBookDb();

        Mockito.when( bookRepository.getAll() ).thenReturn( booksDb );
        Mockito.when( bookRepository.getModifiedAfter( Mockito.any( Instant.class ) ) )
                .thenAnswer( data -> booksDb.stream().filter( b -> b.getModifyDate().isAfter( data.getArgument( 0 ) ) ).collect( Collectors.toList() ) );
        Mockito.when( bookRepository.findSimilar( Mockito.any( BookDto.class ) ) ).thenAnswer( book -> Collections.emptyList() );
        Mockito.when( bookRepository.save( Mockito.any( Book.class ) ) ).thenAnswer( data -> {
            Book book = data.getArgument( 0 );

            if ( book.getId() == null ) {
                book.setId( (new Random()).nextLong() );
            } else {
                booksDb.removeIf( b -> b.getId().equals( book.getId() ) );
            }

            booksDb.add( book );
            return book;
        } );
//        Mockito.when(bookRepository.findSimilar(Mockito.any(BookDto.class))).thenAnswer(book -> Collections.singletonList(new Book()));
    }

    @Test
    void afterSynchronizationEveryLocalBookGetsRemoteId() {
        var list = bookDatabaseSynchronizationService.synchronize( GetDesktopBooks(), Instant.now() );

        for ( var element : list ) {
            Assertions.assertNotNull( element.getRemoteId() );
        }
    }

    @Test
    void afterSynchronizationExistingBookDoesNotChangeId() {
        var inputBooks = GetDesktopBooks();
        var list = bookDatabaseSynchronizationService.synchronize( inputBooks, Instant.now() );

        for ( var element : list ) {
            var originalBook = inputBooks.stream().filter( b -> b.getLocalId().equals( element.getLocalId() ) ).findFirst().orElse( null );
            if ( originalBook != null && originalBook.getRemoteId() != null ) {
                Assertions.assertEquals( originalBook.getRemoteId(), element.getRemoteId() );
            }
        }
    }

    @Test
    void remoteIdIsSetWhenBookExistsOnServer() {
        var inputBooks = GetDesktopBooks();
        var list = bookDatabaseSynchronizationService.synchronize( inputBooks, Instant.now() );
        var existingBook = list.stream().filter( b -> b.getLocalId().equals( 24L ) ).findFirst().orElse( null );
        Assertions.assertEquals( existingBook.getRemoteId(), 1L );
    }

    @Test
    void bookOnServerWithOlderModDateIsModified() {
        var inputBooks = GetDesktopBooks();
        var list = bookDatabaseSynchronizationService.synchronize( inputBooks, Instant.now() );
        var existingBook = bookRepository.getAll().stream().filter( b -> b.getId().equals( 5L ) ).findFirst().orElse( null );
        Assertions.assertEquals( "Title four edit", existingBook.getTitle() );
    }

    @Test
    void bookOnServerWithNewerModDateIsUntouched() {
        var inputBooks = GetDesktopBooks();
        var list = bookDatabaseSynchronizationService.synchronize( inputBooks, Instant.now() );
        var existingBook = bookRepository.getAll().stream().filter( b -> b.getId().equals( 2L ) ).findFirst().orElse( null );
        Assertions.assertEquals( "Title two", existingBook.getTitle() );
    }

    @Test
    void bookInDtoIsModifiedWithNewerDataFromServer() {
        var inputBooks = GetDesktopBooks();
        var list = bookDatabaseSynchronizationService.synchronize( inputBooks, Instant.now() );
        var existingBook = list.stream().filter( b -> b.getRemoteId().equals( 2L ) ).findFirst().orElse( null );
        Assertions.assertEquals( "Title two", existingBook.getTitle() );
    }

    @Test
    void booksInDtoAndOnServerAreTheSameAfterSync() {
        var list = bookDatabaseSynchronizationService.synchronize( GetDesktopBooks(), Instant.now() );
        var books = bookRepository.getAll();
        list.forEach( dto -> {
            var book = books.stream().filter( b -> b.getId().equals( dto.getRemoteId() ) ).findFirst().orElse( null );
            Assertions.assertNotNull( book );
            Assertions.assertEquals( 0, book.compareTo( dto ) );
        } );
    }

    @Test
    void serverAddsAllBooksModifiedAfterLastSyncToDto() {
        var list = bookDatabaseSynchronizationService.synchronize( GetDesktopBooks(), Instant.now() );
        Assertions.assertEquals( 2, list.stream().filter( b -> b.getRemoteId() == 6L || b.getRemoteId() == 2L ).count() );
    }
}
