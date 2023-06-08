package sperka.pl.bookcase.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sperka.pl.bookcase.server.validators.annotations.BookSignatureNotExists;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@BookSignatureNotExists( message = "books.error.signature-exists" )
public class BookDto {
    private Long localId;
    private Long remoteId;
    @NotBlank( message = "books.error.author-may-not-be-blank" )
    private String author;
    @NotBlank( message = "books.error.title-may-not-be-blank" )
    private String title;
    @NotBlank( message = "books.error.release-may-not-be-blank" )
    private String released;
    @NotBlank( message = "books.error.signature-may-not-be-blank" )
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
                ", released='" + released + '\'' +
                ", signature='" + signature + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", deleted=" + deleted +
                '}';
    }
}
