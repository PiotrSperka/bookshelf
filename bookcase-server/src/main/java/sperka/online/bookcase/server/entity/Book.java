package sperka.online.bookcase.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
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
@Table( name = "books" )
@SQLDelete( sql = "UPDATE books SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book implements IdProvider, Comparable< BookDto > {
    @Id
    @SequenceGenerator( name = "bookSeq", sequenceName = "book_id_seq", allocationSize = 1, initialValue = 1 )
    @GeneratedValue( generator = "bookSeq" )
    private Long id;

    private String author;

    private String title;

    @Column( name = "date" )
    private String placeAndYear;

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
        dto.setPlaceAndYear( placeAndYear );
        dto.setDeleted( deleted );

        return dto;
    }

    public void fromDto( BookDto dto ) {
        author = dto.getAuthor();
        createDate = dto.getCreateDate();
        modifyDate = dto.getModifyDate();
        title = dto.getTitle();
        signature = dto.getSignature();
        placeAndYear = dto.getPlaceAndYear();
        deleted = dto.getDeleted();
    }

    @Override
    public int compareTo( BookDto dto ) {
        if ( dto.getModifyDate().equals( getModifyDate() ) ) {
            var same = dto.getAuthor().equals( getAuthor() )
                    && dto.getPlaceAndYear().equals( getPlaceAndYear() )
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
