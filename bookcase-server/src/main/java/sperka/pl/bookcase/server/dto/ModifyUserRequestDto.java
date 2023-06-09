package sperka.pl.bookcase.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModifyUserRequestDto {
    private Long id;
    private String name;
    private List< String > roles;
    private String password;
    private Boolean active;
    private String email;
    private String locale;
}
