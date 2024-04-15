module com.example.real {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;
    requires java.sql;
    requires javafx.web;


    opens com.example.real to javafx.fxml;


    exports com.example.real;
}