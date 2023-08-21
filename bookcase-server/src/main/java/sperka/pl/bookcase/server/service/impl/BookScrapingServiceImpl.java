package sperka.pl.bookcase.server.service.impl;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jetbrains.annotations.NotNull;
import sperka.pl.bookcase.bookscrapers.CzechMedievalSourcesScraper;
import sperka.pl.bookcase.server.dto.BookScrapingJobDto;
import sperka.pl.bookcase.server.entity.BookScrapingJob;
import sperka.pl.bookcase.server.enums.BookScrapingState;
import sperka.pl.bookcase.server.repository.BookScrapingJobRepository;
import sperka.pl.bookcase.server.service.BookScrapingService;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationScoped
public class BookScrapingServiceImpl implements BookScrapingService {
    private final BookScrapingJobRepository bookScrapingJobRepository;

    private final String bookScrapingDirectory;

    @Inject
    public BookScrapingServiceImpl( BookScrapingJobRepository bookScrapingJobRepository, @ConfigProperty( name = "application.bookscraper.directory", defaultValue = "" ) String bookScrapingDirectory ) {
        this.bookScrapingJobRepository = bookScrapingJobRepository;
        this.bookScrapingDirectory = bookScrapingDirectory;
    }

    @Override
    public List< BookScrapingJob > getAll() {
        return bookScrapingJobRepository.getAll();
    }

    @Override
    @Transactional
    public boolean createJob( BookScrapingJobDto dto ) {
        var entity = BookScrapingJob.fromDto( dto );
        bookScrapingJobRepository.save( entity );

        return true;
    }

    @Override
    public InputStream getFile( long id ) throws IOException {
        var job = bookScrapingJobRepository.getById( id );

        if ( job != null && job.getBookScrapingState() == BookScrapingState.READY ) {
            return Files.newInputStream( Path.of( job.getFilePath() ), StandardOpenOption.READ );
        }

        return new ByteArrayInputStream( new byte[ 0 ] );
    }

    // TODO: notification instead of polling
    @Scheduled( every = "1m" )
    @Transactional
    void processJobs() {
        var jobs = bookScrapingJobRepository.getAll();
        if ( jobs.stream().noneMatch( j -> j.getBookScrapingState() == BookScrapingState.PROCESSING ) ) {
            jobs.stream().filter( j -> j.getBookScrapingState() == BookScrapingState.QUEUED ).findAny().ifPresent( this::runBookScraping );
        }
    }

    private void runBookScraping( @NotNull BookScrapingJob job ) {
        job.setBookScrapingState( BookScrapingState.PROCESSING );
        bookScrapingJobRepository.save( job );
        try {
            var title = CzechMedievalSourcesScraper.getBookTitle( job.getInputData() );
            var pdf = CzechMedievalSourcesScraper.getBookAsPdf( job.getInputData() );
            var filename = Instant.now().toEpochMilli() + ".pdf";
            var filepath = Path.of( bookScrapingDirectory, filename ).toString();

            try ( var fileStream = new FileOutputStream( filepath ) ) {
                pdf.writeTo( fileStream );
            }

            job.setTitle( title.isBlank() ? filename : title );
            job.setFilePath( filepath );
            job.setBookScrapingState( BookScrapingState.READY );
            bookScrapingJobRepository.save( job );
        } catch ( IOException ex ) {
            job.setBookScrapingState( BookScrapingState.ERROR );
            bookScrapingJobRepository.save( job );
        }
    }

    @Scheduled( every = "1h" )
    @Transactional
    void bookScrapingJobsCleanup() {
        bookScrapingJobRepository.getAll().stream()
                .filter( j -> j.getBookScrapingState() != BookScrapingState.PROCESSING && j.getCreateDate().isBefore( Instant.now().minus( 24, ChronoUnit.HOURS ) ) )
                .forEach( bookScrapingJobRepository::delete );
    }
}
