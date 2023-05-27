package sperka.pl.bookcase.desktop.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sperka.pl.bookcase.commons.dto.BookDto;

import java.time.Instant;
import java.util.List;

@FeignClient(value = "books", url = "http://localhost:8080")
public interface BookClient {
    @RequestMapping(method = RequestMethod.GET, value = "/books/all")
    List<BookDto> findAll();

    @RequestMapping(method = RequestMethod.GET, value = "/books/after/{date}")
    List<BookDto> findAfter(@PathVariable(name = "date") Instant afterDate);
}
