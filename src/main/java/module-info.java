module org.example.elitedrivingschoolsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires jakarta.persistence;
    requires static lombok;
    requires com.ctc.wstx;
    requires jbcrypt;


    opens lk.ijse.elitedrivingschoolsystem to javafx.fxml;
    exports lk.ijse.elitedrivingschoolsystem;
    exports lk.ijse.elitedrivingschoolsystem.Controller;
    exports lk.ijse.elitedrivingschoolsystem.entity;
    opens lk.ijse.elitedrivingschoolsystem.entity to org.hibernate.orm.core;
    opens lk.ijse.elitedrivingschoolsystem.Controller to javafx.fxml;
    opens lk.ijse.elitedrivingschoolsystem.dto to javafx.base;

}