package com.marajet.todo;

import java.sql.*;
import java.io.*;

public class ListsDriver {

    private static void createLists() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:data/lists")) {
            String sql = "CREATE TABLE IF NOT EXISTS LIST_NAMES (\n" +
                    "list_id integer PRIMARY KEY,\n" +
                    "name text NOT NULL UNIQUE\n" +
                    ");";

            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate(sql);
            }
        }
    }

    private static void deleteLists() throws SQLException {
        File f = new File("data/lists");
        if (f.delete()) {
            System.out.println("Delete successful.");
        } else {
            System.out.println("Delete unsuccessful.");
        }
    }

    public static void main() throws SQLException {
        deleteLists();
        createLists();
    }
}
