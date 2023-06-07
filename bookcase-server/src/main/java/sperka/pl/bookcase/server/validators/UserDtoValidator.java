package sperka.pl.bookcase.server.validators;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import sperka.pl.bookcase.server.dto.CreateUserRequestDto;
import sperka.pl.bookcase.server.dto.ModifyUserRequestDto;
import sperka.pl.bookcase.server.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class UserDtoValidator {
    private final UserRepository userRepository;
    private final Validator validator;

    @Inject
    public UserDtoValidator( UserRepository userRepository, Validator validator ) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    public Map< String, String > validate( CreateUserRequestDto dto ) {
        var result = new HashMap< String, String >();

        for ( var violation : validator.validate( dto ) ) {
            result.put( violation.getPropertyPath().toString(), violation.getMessage() );
        }

        if ( userRepository.getUserByUsername( dto.getName() ) != null ) {
            result.put( "name", "user.error.name-already-exists" );
        }

        if ( userRepository.getUserByEmail( dto.getEmail() ) != null ) {
            result.put( "email", "user.error.email-already-exists" );
        }

        if ( dto.getRoles().isEmpty() ) {
            result.put( "roles", "user.error.user-needs-at-least-one-role" );
        }

        return result;
    }

    public Map< String, String > validate( ModifyUserRequestDto dto ) {
        var result = new HashMap< String, String >();

        for ( var violation : validator.validate( dto ) ) {
            result.put( violation.getPropertyPath().toString(), violation.getMessage() );
        }

        var userByName = userRepository.getUserByUsername( dto.getName() );
        if ( userByName != null && !userByName.getId().equals( dto.getId() ) ) {
            result.put( "name", "user.error.name-already-exists" );
        }

        var userByEmail = userRepository.getUserByEmail( dto.getEmail() );
        if ( userByEmail != null && !userByEmail.getId().equals( dto.getId() ) ) {
            result.put( "email", "user.error.email-already-exists" );
        }

        if ( dto.getRoles() == null || dto.getRoles().isEmpty() ) {
            result.put( "roles", "user.error.user-needs-at-least-one-role" );
        }

        if ( dto.getPassword() != null && !dto.getPassword().isBlank() && dto.getPassword().length() < 6 ) {
            result.put( "password", "user.error.password-needs-to-be-at-least-6-chars" );
        }

        return result;
    }
}
