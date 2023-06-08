package sperka.pl.bookcase.server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitializeUserRequestDto {
    @NotEmpty( message = "user.error.name-must-not-be-empty" )
    private String name;
    @NotEmpty( message = "user.error.password-needs-to-be-at-least-6-chars" )
    private String password;
    @NotEmpty( message = "user.error.email-must-not-be-empty" )
    @Email( message = "user.error.email-must-have-right-format" )
    private String email;
}
