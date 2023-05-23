package sperka.pl.bookcase.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long localId;
    private Long remoteId;
    @NotBlank(message = "Author may not be blank")
    private String author;
    @NotBlank(message = "Title may not be blank")
    private String title;
    @NotBlank(message = "Release may not be blank")
    private String released;
    @NotBlank(message = "Signature may not be blank")
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
