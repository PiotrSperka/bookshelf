package sperka.pl.bookcase.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sperka.pl.bookcase.server.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoDto {
    private Long id;
    private String name;
    private List< String > roles;
    private Boolean active;
    private String email;
    private String locale;

    public static UserInfoDto fromEntity( User user ) {
        var dto = new UserInfoDto();
        dto.setId( user.getId() );
        dto.setName( user.getName() );
        dto.setRoles( Arrays.stream( user.getRoles().split( "," ) ).collect( Collectors.toList() ) );
        dto.setActive( user.getActive() );
        dto.setEmail( user.getEmail() );
        dto.setLocale( user.getLocale() );

        return dto;
    }
}
