package com.example.pianino;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        scene.getStylesheets().add(
                Objects.requireNonNull(Application.class
                                .getResource("keyboard.css"))
                        .toExternalForm()
        );

        scene.getStylesheets().add(
                Objects.requireNonNull(Application.class
                                .getResource("settings.css"))
                        .toExternalForm()
        );

        stage.setTitle("Wirtualne Pianino");
        stage.setScene(scene);
        stage.show();
    }
}
