package ru.iu3.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EqualizerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/ru/iu3/GUI/FXMLDocument.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("EQUALIZER");
        stage.setScene(scene);
        scene.getStylesheets().add(
                EqualizerApp.class.getResource("/ru/iu3/GUI/Style.css").toExternalForm());
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}