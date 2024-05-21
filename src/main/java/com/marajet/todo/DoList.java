package com.marajet.todo;

import java.sql.*;
import java.util.ArrayList;

public class DoList {
    private static final String URL = "jdbc:sqlite:data/lists";
    private final String listName;
    private int listID;
    private Connection conn;
    private int length = 0;
    private int nextTaskID = 1;

    /**
     * Creates or opens a list based on user request, providing a corresponding list
     * object through which the user can manipulate the data.
     *
     * @param listName user-provided name for the list
     * @param request CREATE or OPEN
     * @throws SQLException
     * @throws BadRequestException
     */
    public DoList(String listName, Request request) throws SQLException, BadRequestException {
        this.listName = listName;
        conn = DriverManager.getConnection(URL);

        // Validate request
        int tableID = listExists();
        if (tableID != 0 && request == Request.OPEN) { // table exists and request is OPEN
            listID = tableID;
            open();
        } else if (tableID != 0) { // table exists, request == CREATE is given
            throw new BadRequestException("Bad Request: Cannot create list because list already exists");
        } else if (request == Request.CREATE) { // file does not exist is given
            create();
        } else { // file does not exist and request is OPEN
            throw new BadRequestException("Bad Request: Cannot open list because list does not exist");
        }
    }

    /**
     * Getter for length of DoList
     *
     * @return length of DoList
     */
    public int getLength() {
        return length;
    }

    /**
     * Determines if a list with that (user-provided) name exists and returns the table name.
     *
     * @return Returns tableName in the form of a String if it exists, NULL otherwise.
     * @throws SQLException
     */
    private int listExists() throws SQLException {
        String sql = "SELECT list_id\n" +
                "FROM LIST_NAMES\n" +
                "WHERE name = ?;";

        try (PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.setString(1, listName);
            try (ResultSet result = pStatement.executeQuery()) {
                if (result.next()) {
                    return result.getInt("list_id");
                }
            }
        }

        return 0;
    }

    /**
     * Creates a new table for the list
     *
     * @throws SQLException
     */
    private void create() throws SQLException {
        // first, figure out table name. Find maximum in order to create new (greater) ID
        // if there's no tables, returns 0, so 1 is the first ID
        String sql = "SELECT MAX(list_id) AS maximum\n" +
                "FROM LIST_NAMES;";

        try (Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql)) {
            if (result.next()) {
                listID = result.getInt("maximum") + 1;
            }
        }

        // finally, add table to list of tables
        // note: might want to only add after creation, but then table ID might be lost
        sql = "INSERT INTO LIST_NAMES(list_id, name)\n" +
                "VALUES(?, ?);";

        try (PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.setInt(1, listID);
            pStatement.setString(2, listName);
            pStatement.executeUpdate();
        }

        // create table
        sql = "CREATE TABLE IF NOT EXISTS LIST" + listID + "(\n" +
                "task_id integer PRIMARY KEY,\n" +
                "task text NOT NULL UNIQUE\n" +
                ");";

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    /**
     * Finds the maximum task ID currently in usage
     *
     * @return the value of the max task ID, or 0 if there are no tasks
     * @throws SQLException
     */
    private int findMaxTaskID() throws SQLException {
        String sql = "SELECT MAX(task_id) AS maximum\n" +
                "FROM LIST" + listID + ";";

        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            if (result.next()) {
                return result.getInt("maximum");
            }
        }

        return -1;
    }

    /**
     * "Opens" the list, i.e., prepares the (existing) list to be acted upon by
     * the program which creates the DoList object. Can be opened at creation of
     * DoList or anytime thereafter.
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        // check if conn exists, if not create new one
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL);
        }

        // listID has already been assigned

        // if length != 0, it's been updated before and doesn't need to be queried again
        if (length == 0) {
            // count the number of tasks
            String sql = "SELECT COUNT(*) AS count\n" +
                    "FROM LIST" + listID + ";";

            try (Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {
                if (result.next()) {
                    length = result.getInt("count");
                }
            }
        }

        // if nextTaskID != 1, it's been updated before and doesn't need to be queried again
        if (nextTaskID != 1) {
            nextTaskID = findMaxTaskID() + 1;
        }
    }

    /**
     * Closes the DoList by closing the connection. So that
     * the program which creates the DoList can clean up Connections
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    /**
     * Adds a task to the end of the list
     *
     * @param task String
     * @throws SQLException
     */
    public void add(String task) throws SQLException {
        String sql = "INSERT INTO LIST" + listID + "(task_id, task)\n" +
                "VALUES(?, ?);";

        try (PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.setInt(1, nextTaskID);
            nextTaskID++;
            pStatement.setString(2, task);
            pStatement.executeUpdate();
        }

        length++;
    }

    // TODO write add(int index, String task)

    /**
     * Removes the last item added to the list (ie, the item w/ the greatest task ID)
     * TODO: update so removes item at specified index
     *
     * @param index not important right now, dummy number, to be updated
     */
    public void remove(int index) throws SQLException {
        int maxTaskID = findMaxTaskID();

        if (maxTaskID > 0) {
            String sql = "DELETE FROM LIST" + listID + "\n" +
                    "WHERE task_id = " + maxTaskID + ";";

            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate(sql);
            }

            length--;
        }
    }

    /**
     * Deletes the list. This is a permanent action!
     *
     * @throws SQLException
     */
    public void deleteList() throws SQLException {
        String sql = "DROP TABLE LIST" + listID;

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }

        // closes the list connection
        close();
    }

    /**
     * Returns all the tasks in the form of an ArrayList
     *
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getTasks() throws SQLException {
        ArrayList<String> tasks = new ArrayList<>();
        String sql = "SELECT task FROM LIST" + listID;

        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            while(result.next()) {
                tasks.add(result.getString("task"));
            }
        }

        return tasks;
    }

    // TODO write getTask(int index)

    /**
     * Provides the name of each existing list (in this impl., database)
     *
     * @return ArrayList of String, existing lists
     */
    public static ArrayList<String> getLists() throws SQLException {
        ArrayList<String> lists = new ArrayList<>();
        String sql = "SELECT name FROM LIST_NAMES;";

        try (Connection conn = DriverManager.getConnection(URL);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql)) {
            while(result.next()) {
                lists.add(result.getString("name"));
            }
        }

        return lists;
    }
}
