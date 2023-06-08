package sperka.pl.bookcase.server.mailer;

import sperka.pl.bookcase.server.entity.User;

public interface MailerFacade {
    void sendWelcomeMail( User user );

    void sendPasswordResetMail( User user );
}
