package sperka.online.bookcase.desktop;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {
    private final static Logger logger = LoggerFactory.getLogger(App.class);
    private final static ResourceBundle messages = ResourceBundle.getBundle("MessageBundle", Locale.getDefault());

    private static Scene scene;
    private static ConfigurableApplicationContext context;
    private static Stage stage;

    @Value("${application.name}")
    private String applicationName;

    @Value("${build.version}")
    private String buildVersion;

    @Value("${build.timestamp}")
    private String buildTimestamp;

    public static Scene getScene() {
        return scene;
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader()
                .getResource("fxml/" + fxml + ".fxml"), messages);
        fxmlLoader.setControllerFactory(context::getBean);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        ApplicationContextInitializer<GenericApplicationContext> initializer = ac -> {
            ac.registerBean(Application.class, () -> App.this);
            ac.registerBean(Parameters.class, this::getParameters);
            ac.registerBean(HostServices.class, this::getHostServices);
        };

        context = new SpringApplicationBuilder().sources(Main.class).initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));

        super.init();
    }

    @Override
    public void stop() throws Exception {
        context.close();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;

        scene = new Scene(loadFXML("main"), 800, 600);
        URL mainCssResource = Thread.currentThread().getContextClassLoader().getResource("css/main.css");
        if (mainCssResource != null) {
            scene.getStylesheets().add(mainCssResource.toExternalForm());
        }

        stage.setScene(scene);
        stage.setTitle(MessageFormat.format(messages.getString("app.title"), buildVersion, buildTimestamp));
        stage.show();
    }
}
