module org.warehouse.c {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires mysql.connector.j;
    requires java.desktop;

    opens org.warehouse to javafx.fxml;
    opens org.warehouse.controller to javafx.fxml;

    opens org.warehouse.model to javafx.base;

    exports org.warehouse;
    opens org.warehouse.controller.admin to javafx.fxml;
    opens org.warehouse.controller.user to javafx.fxml;
}