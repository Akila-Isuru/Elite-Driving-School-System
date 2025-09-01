module org.example.elitedrivingschoolsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.ijse.elitedrivingschoolsystem to javafx.fxml;
    exports lk.ijse.elitedrivingschoolsystem;
    exports lk.ijse.elitedrivingschoolsystem.Controller;
}