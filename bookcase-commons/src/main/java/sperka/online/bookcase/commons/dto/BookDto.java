package sperka.online.bookcase.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long localId;
    private Long remoteId;
    private String author;
    private String title;
    private String placeAndYear;
    private String signature;
    private Instant createDate;
    private Instant modifyDate;
    private Boolean deleted;

    @Override
    public String toString() {
        return "BookDto{" +
                "localId=" + localId +
                ", remoteId=" + remoteId +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", placeAndYear='" + placeAndYear + '\'' +
                ", signature='" + signature + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", deleted=" + deleted +
                '}';
    }
}
