package sperka.pl.bookcase.server.validators.annotations;

import sperka.pl.bookcase.server.validators.BookSignatureNotExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
@Constraint( validatedBy = BookSignatureNotExistsValidator.class )
public @interface BookSignatureNotExists {
    String message() default "Book signature already exists";

    Class< ? >[] groups() default {};

    Class< ? extends Payload >[] payload() default {};
}
