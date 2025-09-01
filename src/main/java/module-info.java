module org.example.elitedrivingschoolsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.elitedrivingschoolsystem to javafx.fxml;
    exports org.example.elitedrivingschoolsystem;
}