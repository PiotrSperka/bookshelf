package sperka.pl.bookcase.server.entity;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table( name = "users" )
@Getter
@Setter
@NoArgsConstructor
@UserDefinition
public class User implements IdProvider {
    @Id
    @SequenceGenerator( name = "userSeq", sequenceName = "user_id_seq", allocationSize = 1 )
    @GeneratedValue( generator = "userSeq" )
    private Long id;

    @Username
    @NotNull
    @Column( unique = true )
    private String name;

    @Column( name = "passwd" )
    @Password
    @NotNull
    private String password;

    @Roles
    private String roles;

    @ColumnDefault( "true" )
    private Boolean active;

    private String email;

    private String locale;

    private String resetPasswordToken;

    public void setPassword( String password ) {
        this.password = BcryptUtil.bcryptHash( password );
    }

    public void emptyPassword() {
        this.password = "";
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roles='" + roles + '\'' +
                ", active='" + active + '\'' +
                ", email='" + email + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }

    public static User create( String username, String roles, String email, String locale ) {
        User user = new User();
        user.setName( username );
        user.setRoles( roles );
        user.setActive( true );
        user.setEmail( email );
        user.setLocale( locale );
        return user;
    }

    public boolean isPasswordMatching( String password ) {
        return password != null && BcryptUtil.matches( password, getPassword() );
    }
}
