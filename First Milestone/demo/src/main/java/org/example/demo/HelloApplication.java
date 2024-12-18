package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("XML Editor");
        Image icon = new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("/icons/xml_5105250.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

}
