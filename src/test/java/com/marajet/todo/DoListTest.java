package com.marajet.todo;

import java.sql.*;

public class DoListTest {
    public static void testRequestExceptions() throws SQLException {
        // Test 1: non-existing list + create
        try {
            new DoList("test", Request.CREATE);
            System.out.println("Test 1 (create): Success");
        } catch (BadRequestException e) {
            System.out.println("Test 1 (create): Failure");
        }

        // Test 2: existing list + open
        try {
            new DoList("test", Request.OPEN);
            System.out.println("Test 2 (open): Success");
        }  catch (BadRequestException e) {
            System.out.println("Test 2 (open): Failure");
        }

        // Test 3: non-existing list + open
        try {
            new DoList("test2", Request.OPEN);
            System.out.println("Test 3 (bad open): Failure");
        }  catch (BadRequestException e) {
            System.out.println("Test 3 (bad open): Success");
        }

        // Test 4: existing list + create
        try {
            new DoList("test", Request.CREATE);
            System.out.println("Test 4 (bad create): Failure");
        }  catch (BadRequestException e) {
            System.out.println("Test 4 (bad create): Success");
        }
    }

    private static void getListsTest() throws SQLException {
        /*try {
            for (int i = 0; i < 5; i++) {
                new DoList("test" + i, Request.CREATE);
            }
        } catch (BadRequestException e) {
            System.out.println("Bad requests...");
        }*/

        System.out.println(DoList.getLists());
    }

    private static void addRemoveTest() throws SQLException {
        try {
            DoList myList = new DoList("test", Request.CREATE);

            System.out.println();
            System.out.println("Getting tasks immediately...");
            System.out.println(myList.getTasks());

            System.out.println();
            System.out.println("Adding one task...");
            myList.add("task 1");
            System.out.println(myList.getTasks());

            System.out.println();
            System.out.println("Adding four more tasks...");
            myList.add("task 2");
            myList.add("task buggaboo");
            myList.add("task yay");
            myList.add("task 5");
            System.out.println(myList.getTasks());

            System.out.println();
            System.out.println("Removing a task...");
            myList.remove(3);
            System.out.println(myList.getTasks());

            System.out.println();
            System.out.println("Removing another task...");
            myList.remove(-5000);
            System.out.println(myList.getTasks());

            System.out.println();
            System.out.println("Removing the last three tasks...");
            myList.remove(0);
            myList.remove(0);
            myList.remove(0);
            System.out.println(myList.getTasks());

            System.out.println();
            System.out.println("Attempting to remove tasks from an empty list");
            myList.remove(0);
            System.out.println(myList.getTasks());

            System.out.println();
            System.out.println("Adding back in a final task");
            myList.add("yayayay!");
            System.out.println(myList.getTasks());

            System.out.println();
            System.out.println("Closing the list, then opening a new one to access same list");
            myList.close();
            DoList myNewList = new DoList("test", Request.OPEN);
            System.out.println(myNewList.getTasks());

            System.out.println();
            System.out.println("Closing the list, then re-opening the same list object, then printing");
            myNewList.close();
            myNewList.open();
            System.out.println(myNewList.getTasks());
        } catch (BadRequestException e) {
            System.out.println("Bad request... test/CREATE");
        }
    }

    private static void useClosedList() throws SQLException {
        try {
            DoList myList = new DoList("test", Request.CREATE);
            myList.close();
            myList.getTasks();
        } catch (BadRequestException e) {
            System.out.println("Bad request...");
        }
    }

    public static void main(String[] args) throws SQLException {
        useClosedList();
    }
}
