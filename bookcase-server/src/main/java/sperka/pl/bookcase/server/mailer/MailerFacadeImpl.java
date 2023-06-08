package sperka.pl.bookcase.server.mailer;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sperka.pl.bookcase.server.entity.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@Slf4j
public class MailerFacadeImpl implements MailerFacade {
    private final Mailer mailer;

    private final Engine engine;

    private final String applicationUrl;

    @Inject
    public MailerFacadeImpl( Mailer mailer, Engine engine, @ConfigProperty( name = "application.url" ) String applicationUrl ) {
        this.mailer = mailer;
        this.engine = engine;
        this.applicationUrl = applicationUrl;
    }

    private String loadTemplate( String locale, String templateName, Map< String, String > data, String extension ) throws IOException {
        var templateStream = MailerFacadeImpl.class.getResourceAsStream( "/templates/" + templateName + "_" + locale + "." + extension );
        if ( templateStream == null ) {
            templateStream = MailerFacadeImpl.class.getResourceAsStream( "/templates/" + templateName + "." + extension ); // default fallback
        }
        if ( templateStream == null ) {
            throw new IllegalArgumentException( "Templates does not exist." );
        }
        try {
            Template t = engine.parse( new String( templateStream.readAllBytes(), StandardCharsets.UTF_8 ) );
            return t.data( data ).render();
        } catch ( Exception e ) {
            throw new IllegalArgumentException( "Templates does not exist." );
        } finally {
            templateStream.close();
        }
    }

    private String loadHtmlTemplate( String locale, String templateName, Map< String, String > data ) throws IOException {
        return loadTemplate( locale, templateName, data, "html" );
    }

    @Override
    public void sendWelcomeMail( User user ) {
        try {
            var data = new HashMap< String, String >();
            data.put( "name", user.getName() );
            data.put( "activation_url", applicationUrl + "/set-password/" + user.getResetPasswordToken() );
            var template = loadHtmlTemplate( user.getLocale(), "welcome-mail", data );
            mailer.send( Mail.withHtml( user.getEmail(),
                    loadTemplate( user.getLocale(), "welcome-mail-subject", Collections.emptyMap(), "txt" ), template ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    @Override
    public void sendPasswordResetMail( User user ) {
        try {
            var data = new HashMap< String, String >();
            data.put( "name", user.getName() );
            data.put( "activation_url", applicationUrl + "/set-password/" + user.getResetPasswordToken() );
            var template = loadHtmlTemplate( user.getLocale(), "reset-password-mail", data );
            mailer.send( Mail.withHtml( user.getEmail(),
                    loadTemplate( user.getLocale(), "reset-password-mail-subject", Collections.emptyMap(), "txt" ), template ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }
}
