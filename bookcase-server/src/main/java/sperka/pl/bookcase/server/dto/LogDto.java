package sperka.pl.bookcase.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sperka.pl.bookcase.server.entity.Log;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogDto {
    private Long id;
    private String log;
    private String username;
    private Instant date;

    public static LogDto fromEntity( Log log ) {
        return new LogDto( log.getId(), log.getLog(), log.getUsername(), log.getDate() );
    }
}
