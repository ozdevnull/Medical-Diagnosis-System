module com.mds {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mds to javafx.fxml;
    opens com.mds.controller to javafx.fxml;

    exports com.mds;
    exports com.mds.controller;
    exports com.mds.database;
    exports com.mds.model;
    exports com.mds.dao;
    exports com.mds.service;
}
