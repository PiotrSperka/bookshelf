package sperka.online;

import sperka.online.bookcase.server.entity.Book;
import sperka.online.bookcase.server.repository.BookRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello-resteasy")
public class GreetingResource {
    @Inject
    BookRepository bookRepository;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        Book book = bookRepository.getById(100L);
        return book.getTitle();
    }
}