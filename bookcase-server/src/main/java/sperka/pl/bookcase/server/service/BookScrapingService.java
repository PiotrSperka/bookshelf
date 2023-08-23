package sperka.pl.bookcase.server.service;

import sperka.pl.bookcase.server.dto.BookScrapingJobDto;
import sperka.pl.bookcase.server.entity.BookScrapingJob;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface BookScrapingService {
    List< BookScrapingJob > getAll();

    boolean createJob( BookScrapingJobDto dto );

    InputStream getFile( long id ) throws IOException;
}
