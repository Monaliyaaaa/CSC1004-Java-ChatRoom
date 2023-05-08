module com.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires java.desktop;
    requires jdk.jfr;

    opens com.login to javafx.fxml;
    exports com.login;
    exports com;
    opens com to javafx.fxml;

    opens com.register to javafx.fxml;
    exports com.register;

    opens com.chat to javafx.fxml;
    exports com.chat;
}