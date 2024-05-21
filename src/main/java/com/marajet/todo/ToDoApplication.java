package com.marajet.todo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ToDoApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // TODO: handle that exception better
        Parent root = FXMLLoader.load(getClass().getResource("todo.fxml"));
        stage.setTitle("DoLists");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("todo.css").toExternalForm());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
