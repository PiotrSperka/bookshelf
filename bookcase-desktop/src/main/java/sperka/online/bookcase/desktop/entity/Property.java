package sperka.online.bookcase.desktop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
public class Property implements IdProvider {
    @Id
    @SequenceGenerator(name = "propertySeq", sequenceName = "property_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "propertySeq")
    private Long id;
    private String key;
    @Lob
    private String value;

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.parse(value);
    }

    public void fromLocalDateTime(LocalDateTime localDateTime) {
        value = localDateTime.toString();
    }
}
