package sperka.pl.bookcase.desktop.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sperka.pl.bookcase.commons.dto.BookDto;

import java.time.Instant;
import java.util.List;

@FeignClient( value = "sync", url = "localhost:8080" )
public interface SynchronizationClient {
    @RequestMapping( method = RequestMethod.GET, value = "/sync/{lastSyncTime}" )
    List< BookDto > synchronize( List< BookDto > books, @PathVariable( "lastSyncTime" ) Instant lastSyncTime );
}
