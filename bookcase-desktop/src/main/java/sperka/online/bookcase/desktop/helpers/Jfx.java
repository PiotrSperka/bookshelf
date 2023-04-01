package sperka.online.bookcase.desktop.helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.ResourceBundle;

public class Jfx {
    public static Parent loadFXML( String fxml, ResourceBundle messages, ConfigurableApplicationContext context ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader( Thread.currentThread().getContextClassLoader()
                .getResource( "fxml/" + fxml + ".fxml" ), messages );
        fxmlLoader.setControllerFactory( context::getBean );
        return fxmlLoader.load();
    }

    public static FXMLLoader getFxmlLoader( String fxml, ResourceBundle messages, ConfigurableApplicationContext context ) {
        FXMLLoader fxmlLoader = new FXMLLoader( Thread.currentThread().getContextClassLoader()
                .getResource( "fxml/" + fxml + ".fxml" ), messages );
        fxmlLoader.setControllerFactory( context::getBean );
        return fxmlLoader;
    }
}
