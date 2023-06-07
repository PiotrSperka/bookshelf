package sperka.pl.bookcase.server.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotEmpty( message = "user.error.name-must-not-be-empty" )
    private String name;
    @NotNull( message = "user.error.roles-must-not-be-empty" )
    private List< String > roles;
    @NotEmpty( message = "user.error.email-must-not-be-empty" )
    private String email;
    private String locale;
}
