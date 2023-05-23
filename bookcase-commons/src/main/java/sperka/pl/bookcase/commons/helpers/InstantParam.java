package sperka.pl.bookcase.commons.helpers;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class InstantParam {
    private final Instant date;

    public InstantParam(String dateStr) throws DateTimeParseException {
        if (dateStr == null || dateStr.isBlank()) {
            this.date = null;
            return;
        }

        this.date = Instant.parse(dateStr);
    }

    public Instant getInstant() {
        return date;
    }
}
