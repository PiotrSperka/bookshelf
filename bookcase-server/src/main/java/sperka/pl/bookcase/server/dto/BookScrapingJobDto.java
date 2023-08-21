package sperka.pl.bookcase.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookScrapingJobDto {
    private Long id;
    private String bookScraperType;
    private String bookScrapingState;
    private Instant createDate;
    private String inputData;

    @Override
    public String toString() {
        return "BookScrapingJobDto{" +
                "id=" + id +
                ", bookScraperType='" + bookScraperType + '\'' +
                ", bookScrapingState='" + bookScrapingState + '\'' +
                ", createDate=" + createDate +
                ", inputData='" + inputData + '\'' +
                '}';
    }
}
