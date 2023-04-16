package com.svalero.spaceexplorer.api;

import com.svalero.spaceexplorer.api.controller.AppController;
import com.svalero.spaceexplorer.api.util.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("main-app.fxml"));
        loader.setController(new AppController());
        VBox vbox = loader.load();
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Space Explorer");
        stage.show();
    }

}