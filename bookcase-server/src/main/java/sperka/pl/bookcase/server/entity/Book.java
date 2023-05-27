package sperka.pl.bookcase.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import sperka.pl.bookcase.server.dto.BookDto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table( name = "books" )
@SQLDelete( sql = "UPDATE books SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book implements IdProvider, Comparable<BookDto> {
    @Id
    @SequenceGenerator( name = "bookSeq", sequenceName = "book_id_seq", allocationSize = 1 )
    @GeneratedValue( generator = "bookSeq" )
    private Long id;

    private String author;

    private String title;

    private String released;

    private String signature;

    @Column( name = "create_date" )
    private Instant createDate;

    @Column( name = "modify_date" )
    private Instant modifyDate;

    @Column( columnDefinition = "boolean default false" )
    @JsonIgnore
    private Boolean deleted;

    public BookDto toDto() {
        BookDto dto = new BookDto();

        dto.setRemoteId( id );
        dto.setAuthor( author );
        dto.setCreateDate( createDate );
        dto.setModifyDate( modifyDate );
        dto.setTitle( title );
        dto.setSignature( signature );
        dto.setReleased( released );
        dto.setDeleted( deleted );

        return dto;
    }

    public void fromDto( BookDto dto ) {
        author = dto.getAuthor();
        createDate = dto.getCreateDate();
        modifyDate = dto.getModifyDate();
        title = dto.getTitle();
        signature = dto.getSignature();
        released = dto.getReleased();
        deleted = dto.getDeleted();
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", released='" + released + '\'' +
                ", signature='" + signature + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public int compareTo( BookDto dto ) {
        if ( dto.getModifyDate().equals( getModifyDate() ) ) {
            var same = dto.getAuthor().equals( getAuthor() )
                    && dto.getReleased().equals( getReleased() )
                    && dto.getDeleted().equals( getDeleted() )
                    && dto.getSignature().equals( getSignature() )
                    && dto.getTitle().equals( getTitle() )
                    && dto.getRemoteId().equals( getId() );
            return same ? 0 : 1;
        } else {
            return dto.getModifyDate().compareTo( getModifyDate() );
        }
    }
}
