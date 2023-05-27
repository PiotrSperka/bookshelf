package sperka.pl.bookcase.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "jwt_blacklist" )
@Getter
@Setter
@NoArgsConstructor
public class JwtBlacklist implements IdProvider {
    @Id
    @SequenceGenerator( name = "jwtBlacklistSeq", sequenceName = "jwt_blacklist_id_seq", allocationSize = 1 )
    @GeneratedValue( generator = "jwtBlacklistSeq" )
    private Long id;

    private String jwt;
}
