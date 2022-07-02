package sperka.online.bookcase.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class BookDto {
    private Long localId;
    private Long remoteId;
    private String author;
    private String title;
    private String placeAndYear;
    private String signature;
    private Instant createDate;
    private Instant modifyDate;
}
