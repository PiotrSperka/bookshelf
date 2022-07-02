package sperka.online.bookcase.server.helpers;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.format.DateTimeParseException;

public class InstantParam {
    private final Instant date;

    public InstantParam(String dateStr) throws WebApplicationException {
        if (dateStr == null || dateStr.isBlank()) {
            this.date = null;
            return;
        }

        try {
            this.date = Instant.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Couldn't parse date string: " + e.getMessage())
                    .build());
        }
    }

    public Instant getInstant() {
        return date;
    }
}
