package sperka.online.bookcase.server.entity;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    public String roles;

    public void setPassword( String password ) {
        this.password = BcryptUtil.bcryptHash( password );
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }

    public static User create( String username, String password, String roles ) {
        User user = new User();
        user.setName( username );
        user.setPassword( password );
        user.setRoles( roles );
        return user;
    }

    public boolean isPasswordMatching( String password ) {
        return password != null && BcryptUtil.matches( password, getPassword() );
    }
}
