module com.marajet.todo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.marajet.todo to javafx.fxml;
    exports com.marajet.todo;
}