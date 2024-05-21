package com.marajet.todo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import java.util.ArrayList;

import java.sql.SQLException;

public class ToDoController {
    @FXML
    private VBox sidebar;
    @FXML
    private GridPane listPane;

    /*public void openListClick(ActionEvent e) {
        try {
            DoList list = new DoList(listName, Request.OPEN);
            ArrayList<String> tasks = list.getTasks();
            for (String task : tasks) {
                Label label = new Label(task);
                listPane.getChildren().add(label);
            }
        } catch (BadRequestException e) {
            // TODO handle
        } catch (SQLException e) {
            Label label = new Label("Couldn't load list...");
            listPane.getChildren().add(label);
            // TODO handle
        }
    }*/

    @FXML
    public void initialize() {
        try {
            ArrayList<String> lists = DoList.getLists();
            for (String list : lists) {
                Label label = new Label(list);
                sidebar.getChildren().add(label);
            }
        } catch (SQLException e) {
            Label label = new Label("Could not load lists...");
            sidebar.getChildren().add(label);
            // TODO log exception properly
        }
    }
}
