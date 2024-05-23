package com.marajet.todo;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

import java.sql.SQLException;

public class ToDoController {
    @FXML
    private VBox sidebar;
    @FXML
    private VBox listPane;

    @FXML
    public void initialize() {
        try {
            ArrayList<String> lists = DoList.getLists();
            for (String list : lists) {
                Label label = new Label(list);
                label.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    listPane.getChildren().clear();
                    try {
                        DoList list1 = new DoList(list, Request.OPEN);
                        ArrayList<String> tasks = list1.getTasks();
                        for (String task : tasks) {
                            Label label1 = new Label(task);
                            listPane.getChildren().add(label1);
                        }
                    } catch (BadRequestException e) {
                        // TODO handle
                    } catch (SQLException e) {
                        // TODO handle
                    }
                });
                sidebar.getChildren().add(label);
            }
        } catch (SQLException e) {
            Label label = new Label("Could not load lists...");
            sidebar.getChildren().add(label);
            // TODO log exception properly
        }
    }
}
