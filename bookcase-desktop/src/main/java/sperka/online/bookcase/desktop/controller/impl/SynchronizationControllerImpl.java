package sperka.online.bookcase.desktop.controller.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import sperka.online.bookcase.commons.dto.BookDto;
import sperka.online.bookcase.desktop.client.BookClient;
import sperka.online.bookcase.desktop.controller.SynchronizationController;
import sperka.online.bookcase.desktop.entity.Book;
import sperka.online.bookcase.desktop.entity.Property;
import sperka.online.bookcase.desktop.service.BookService;
import sperka.online.bookcase.desktop.service.PropertyService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Controller
public class SynchronizationControllerImpl implements SynchronizationController {
    private final Logger logger = LoggerFactory.getLogger(SynchronizationControllerImpl.class);
    private final BookService bookService;
    private final PropertyService propertyService;
    private final BookClient bookClient;

    public SynchronizationControllerImpl(BookService bookService, PropertyService propertyService, BookClient bookClient) {
        this.bookService = bookService;
        this.propertyService = propertyService;
        this.bookClient = bookClient;
    }

    @Override
    public void synchronize() {
        LocalDateTime synchronizationStartDateTime = LocalDateTime.now();
        LocalDateTime lastSynchronizationDateTime = LocalDateTime.of(0, 1, 1, 0, 0);
        Property lastSynchronizationProperty = propertyService.getProperty("lastSynchronization");
        if (lastSynchronizationProperty != null) {
            lastSynchronizationDateTime = lastSynchronizationProperty.toLocalDateTime();
        }

        try {
            sendCreatedBooksToServer();
            getNewBooksFromServer(lastSynchronizationDateTime);

//            propertyService.setProperty("lastSynchronization", synchronizationStartDateTime.toString());
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
    }

    private void sendCreatedBooksToServer() {

    }

    private void getNewBooksFromServer(LocalDateTime lastSyncTime) {
        List<BookDto> books = bookClient.findAfter(lastSyncTime.toInstant(ZoneOffset.UTC));
        books.forEach( dto -> {
            Book book = Book.fromDto(dto);
            if (!bookService.exists(book)) {
                bookService.save(book);
            }
        } );
    }
}
