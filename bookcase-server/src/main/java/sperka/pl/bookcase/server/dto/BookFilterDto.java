package sperka.pl.bookcase.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookFilterDto {
    private String author;
    private String title;
    private String release;
    private String signature;
    private String sortField;
    private String sortDirection;
}
