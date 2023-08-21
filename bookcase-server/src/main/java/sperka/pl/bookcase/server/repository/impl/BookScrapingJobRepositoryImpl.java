package sperka.pl.bookcase.server.repository.impl;

import jakarta.enterprise.context.ApplicationScoped;
import sperka.pl.bookcase.server.entity.BookScrapingJob;
import sperka.pl.bookcase.server.repository.BookScrapingJobRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class BookScrapingJobRepositoryImpl extends BasicRepositoryImpl< BookScrapingJob > implements BookScrapingJobRepository {
    public BookScrapingJobRepositoryImpl() {
        super( BookScrapingJob.class );
    }

    @Override
    public void delete( Long id ) {
        var entity = getById( id );
        delete( entity );
    }

    @Override
    public void delete( BookScrapingJob entity ) {
        super.delete( entity );

        try {
            Files.deleteIfExists( Path.of( entity.getFilePath() ) );
        } catch ( IOException ex ) {
            System.err.println( ex.getMessage() );
        }
    }
}
