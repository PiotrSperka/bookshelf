package sperka.pl.bookcase.server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "jwt_blacklist")
@Getter
@Setter
@NoArgsConstructor
public class JwtBlacklist implements IdProvider {
    @Id
    @SequenceGenerator( name = "jwtBlacklistSeq", sequenceName = "jwt_blacklist_id_seq", allocationSize = 1, initialValue = 1 )
    @GeneratedValue( generator = "jwtBlacklistSeq" )
    private Long id;

    private String jwt;
}
