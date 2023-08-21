package sperka.pl.bookcase.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;
import sperka.pl.bookcase.server.dto.BookScrapingJobDto;
import sperka.pl.bookcase.server.enums.BookScraperType;
import sperka.pl.bookcase.server.enums.BookScrapingState;

import java.time.Instant;

@Entity
@Table( name = "book_scraping_jobs" )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookScrapingJob implements IdProvider {
    @Id
    @SequenceGenerator( name = "bookScrapingJobsSeq", sequenceName = "book_scraping_jobs_id_seq", allocationSize = 1 )
    @GeneratedValue( generator = "bookScrapingJobsSeq" )
    private Long id;

    @Column( length = 1024, columnDefinition = "Text" )
    private String title;

    @Enumerated( EnumType.STRING )
    @Column( nullable = false )
    private BookScraperType bookScraperType;

    @Enumerated( EnumType.STRING )
    @Column( nullable = false )
    private BookScrapingState bookScrapingState;

    @CreationTimestamp
    @Column( nullable = false )
    private Instant createDate;

    @Column( nullable = false, length = 65535, columnDefinition = "Text" )
    private String inputData;

    @Column( length = 2048, columnDefinition = "Text" )
    private String filePath;

    public BookScrapingJobDto toDto() {
        return new BookScrapingJobDto( id, title != null ? title : "", bookScraperType.name(), bookScrapingState.name(), createDate, inputData );
    }

    public static @NotNull BookScrapingJob fromDto( @NotNull BookScrapingJobDto dto ) {
        var job = new BookScrapingJob();
        job.bookScraperType = BookScraperType.valueOf( dto.getBookScraperType() );
        job.bookScrapingState = BookScrapingState.QUEUED;
        job.inputData = dto.getInputData();

        return job;
    }
}
