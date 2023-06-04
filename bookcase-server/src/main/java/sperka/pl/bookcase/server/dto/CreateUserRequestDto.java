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
public class CreateUserRequestDto {
    private String name;
    private String password;
    private List< String > roles;
    private String email;
    private String locale;
}
