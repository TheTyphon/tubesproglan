module com.example.kataloglaptop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.kataloglaptop to javafx.fxml;
    exports com.example.kataloglaptop;
}