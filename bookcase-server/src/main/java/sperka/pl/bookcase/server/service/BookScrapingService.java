package sperka.pl.bookcase.server.service;

import sperka.pl.bookcase.server.dto.BookScrapingJobDto;
import sperka.pl.bookcase.server.entity.BookScrapingJob;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BookScrapingService {
    List< BookScrapingJob > getAll();

    boolean createJob( BookScrapingJobDto dto );

    File getFile( long id ) throws IOException;
}
