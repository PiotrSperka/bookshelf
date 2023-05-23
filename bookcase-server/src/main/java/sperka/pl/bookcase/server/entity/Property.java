package sperka.pl.bookcase.server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    @Column(name="_key")
    private String key;
    @Lob
    @Column(name = "val")
    private String value;
}
