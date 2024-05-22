package com.marajet.todo;

import java.sql.SQLException;

public class ToDoControllerTest {
    public static void sidebarLists() throws SQLException {
        // reset all lists
        ListsDriver.main();

        // create some new lists
        try {
            DoList chores = new DoList("Chores", Request.CREATE);
            chores.add("clean dishes");
            chores.add("wash sheets");
            DoList homework = new DoList("Homework", Request.CREATE);
            homework.add("write essay");
            new DoList("Whatever else plus this is a very long thing to test text overrun etc etc", Request.CREATE);
        } catch (BadRequestException e) {
            System.out.println("Bad requests...");
        }
        System.out.println(DoList.getLists());

        // run the application and see if it displays properly
        ToDoApplication.main(new String[0]);
    }

    public static void main(String[] args) throws SQLException {
        sidebarLists();
    }
}
