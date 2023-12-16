package com.github.romanqed;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var loader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        var node = (Parent) loader.load();
        var controller = (MainController) node.getUserData();
        controller.setStage(stage);
        var scene = new Scene(node, 1280, 720);
        stage.setTitle("CSG");
        stage.setScene(scene);
        stage.show();
    }
}
