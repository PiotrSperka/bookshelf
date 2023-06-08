package sperka.pl.bookcase.server.validators;

import sperka.pl.bookcase.server.dto.BookDto;
import sperka.pl.bookcase.server.repository.BookRepository;
import sperka.pl.bookcase.server.validators.annotations.BookSignatureNotExists;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@ApplicationScoped
public class BookSignatureNotExistsValidator implements ConstraintValidator< BookSignatureNotExists, BookDto > {
    private final BookRepository bookRepository;

    @Inject
    public BookSignatureNotExistsValidator( BookRepository bookRepository ) {
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean isValid( BookDto b, ConstraintValidatorContext constraintValidatorContext ) {
        var book = bookRepository.getBySignature( b.getSignature() );
        return book == null || book.getId().equals( b.getRemoteId() );
    }
}
