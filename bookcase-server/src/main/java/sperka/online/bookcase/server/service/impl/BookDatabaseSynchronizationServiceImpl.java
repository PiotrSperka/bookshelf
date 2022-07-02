package sperka.online.bookcase.server.service.impl;

import sperka.online.bookcase.commons.dto.BookDto;
import sperka.online.bookcase.server.entity.Book;
import sperka.online.bookcase.server.repository.BookRepository;
import sperka.online.bookcase.server.service.BookDatabaseSynchronizationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BookDatabaseSynchronizationServiceImpl implements BookDatabaseSynchronizationService {
    @Inject
    BookRepository bookRepository;

    @Override
    public List<Book> synchronize(List<BookDto> books) {
        List<Book> saved = new ArrayList<>();

        books.forEach(book -> {
            List<Book> similarBooks = bookRepository.findSimilar(book);
            if (similarBooks.isEmpty()) {
                Book entity = new Book();
                entity.setAuthor(book.getAuthor());
                entity.setTitle(book.getTitle());
                entity.setSignature(book.getSignature());
                entity.setPlaceAndYear(book.getPlaceAndYear());
                entity.setDeleted(false);
                entity.setCreateDate(book.getCreateDate());
                entity.setModifyDate(book.getModifyDate());

                saved.add(bookRepository.save(entity));
            } else {
                // TODO:
            }
        });

        return saved;
    }
}
