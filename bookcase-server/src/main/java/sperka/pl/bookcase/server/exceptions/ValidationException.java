package sperka.pl.bookcase.server.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map< String, String > violations;

    public ValidationException() {
        this.violations = new HashMap<>();
    }

    public ValidationException( Map< String, String > violations ) {
        this.violations = violations;
    }

    public Map< String, String > getViolations() {
        return violations;
    }

    public void addViolation( String field, String message ) {
        violations.put( field, message );
    }

    public boolean isEmpty() {
        return violations.isEmpty();
    }
}
