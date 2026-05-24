module com.example.pianino {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.pianino to javafx.fxml;
    exports com.example.pianino;
    exports com.example.pianino.core;
    exports com.example.pianino.ui;
    opens com.example.pianino.ui to javafx.fxml;
    exports com.example.pianino.ui.recordings;
    opens com.example.pianino.ui.recordings to javafx.fxml;
}