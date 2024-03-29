package sperka.pl.bookcase.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table( name = "logs" )
@Getter
@Setter
@NoArgsConstructor
public class Log implements IdProvider {
    @Id
    @SequenceGenerator( name = "logsSeq", sequenceName = "logs_id_seq", allocationSize = 1 )
    @GeneratedValue( generator = "logsSeq" )
    private Long id;

    private String username;

    @CreationTimestamp
    private Instant date;

    @Column( length = 16777215, columnDefinition = "Mediumtext" )
    private String log;
}
