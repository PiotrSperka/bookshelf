package sperka.online.bookcase.desktop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import sperka.online.bookcase.commons.dto.BookDto;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "books")
@SQLDelete(sql = "UPDATE books SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class Book implements IdProvider {
    @Id
    @SequenceGenerator(name = "bookSeq", sequenceName = "book_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "bookSeq")
    private Long id;

    private String author;

    private String title;

    @Column(name = "date")
    private String placeAndYear;

    private String signature;

    @CreationTimestamp
    @Column(name = "create_date")
    private Instant createDate;

    @UpdateTimestamp
    @Column(name = "modify_date")
    private Instant modifyDate;

    @Column(columnDefinition = "boolean default false")
    @JsonIgnore
    private Boolean deleted;

    public BookDto toDto() {
        BookDto dto = new BookDto();

        dto.setRemoteId(id);
        dto.setAuthor(author);
        dto.setCreateDate(createDate);
        dto.setModifyDate(modifyDate);
        dto.setTitle(title);
        dto.setSignature(signature);
        dto.setPlaceAndYear(placeAndYear);

        return dto;
    }

    public static Book fromDto(BookDto dto) {
        Book book = new Book();

        book.setAuthor(dto.getAuthor());
        book.setDeleted(false);
        book.setCreateDate(dto.getCreateDate());
        book.setModifyDate(dto.getModifyDate());
        book.setTitle(dto.getTitle());
        book.setPlaceAndYear(dto.getPlaceAndYear());
        book.setSignature(dto.getSignature());

        return book;
    }
}
