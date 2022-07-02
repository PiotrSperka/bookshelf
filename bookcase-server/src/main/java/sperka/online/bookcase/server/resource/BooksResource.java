package sperka.online.bookcase.server.resource;

import org.jboss.logging.Logger;
import sperka.online.bookcase.commons.dto.BookDto;
import sperka.online.bookcase.server.entity.Book;
import sperka.online.bookcase.server.helpers.InstantParam;
import sperka.online.bookcase.server.repository.BookRepository;
import sperka.online.bookcase.server.service.BookDatabaseSynchronizationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BooksResource {
    private static final Logger LOG = Logger.getLogger(BooksResource.class);
    private final BookRepository bookRepository;
    private final BookDatabaseSynchronizationService bookDatabaseSynchronizationService;

    @Inject
    public BooksResource(BookRepository bookRepository, BookDatabaseSynchronizationService bookDatabaseSynchronizationService) {
        this.bookRepository = bookRepository;
        this.bookDatabaseSynchronizationService = bookDatabaseSynchronizationService;
    }

    @GET
    @Path("/all")
    public List<BookDto> list() {
        return bookRepository.getAll().stream().map(Book::toDto).collect(Collectors.toList());
    }

    @GET
    @Path("/id/{id}")
    public BookDto byId(@PathParam("id") Long id) {
        Book book = bookRepository.getById(id);
        return (book != null) ? book.toDto() : null;
    }

    @GET
    @Path("/after/{date}")
    public List<BookDto> afterDate(@PathParam("date") InstantParam date) {
        return bookRepository.getCreatedAfter(date.getInstant()).stream().map(Book::toDto).collect(Collectors.toList());
    }

    @GET
    @Path("/last")
    public BookDto last() {
        Book book = bookRepository.getLastModified();
        return (book != null) ? book.toDto() : null;
    }

//    @GET
//    @Path("/migrate")
//    @Transactional
//    public boolean migrate() {
//        List<Book> books = bookRepository.getAll();
//        books.forEach( book -> {
//            if (book.getCreateDate() == null && book.getModifyDate() == null) {
//                String timestamp = book.getTimestamp().toString();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//                try {
//                    Instant timestampInstant = sdf.parse(timestamp).toInstant();
//                    book.setCreateDate(timestampInstant);
//                    book.setModifyDate(timestampInstant);
//                    bookRepository.save(book);
//                } catch (ParseException e) {
//                    LOG.error(e.getMessage(), e);
//                }
//            }
//        });
//
//        return true;
//    }

    @POST
    @Path("/sync")
    public List<BookDto> syncStart(List<BookDto> books) {
        return bookDatabaseSynchronizationService.synchronize(books).stream().map(Book::toDto)
                .collect(Collectors.toList());
    }
}
