module com.example.kalk {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.kalk to javafx.fxml;
    exports com.example.kalk;
}