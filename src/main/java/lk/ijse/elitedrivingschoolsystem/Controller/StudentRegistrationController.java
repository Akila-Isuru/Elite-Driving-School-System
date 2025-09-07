package lk.ijse.elitedrivingschoolsystem.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lk.ijse.elitedrivingschoolsystem.bo.BOFactory;
import lk.ijse.elitedrivingschoolsystem.bo.BOTypes;
import lk.ijse.elitedrivingschoolsystem.bo.custom.StudentBO;
import lk.ijse.elitedrivingschoolsystem.bo.exception.RegistrationException;
import lk.ijse.elitedrivingschoolsystem.dto.StudentDTO;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentRegistrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtStudentId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtContactNumber;

    @FXML
    private CheckBox chkC1001;

    @FXML
    private CheckBox chkC1002;

    @FXML
    private CheckBox chkC1003;

    @FXML
    private CheckBox chkC1004;

    @FXML
    private CheckBox chkC1005;

    private final StudentBO studentBO = (StudentBO) BOFactory.getInstance().getBO(BOTypes.STUDENT);

    @FXML
    void handleRegisterButton(ActionEvent event) {
        // Get data from UI components
        String studentId = txtStudentId.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String contactNumber = txtContactNumber.getText();

        // Get selected course IDs
        List<String> selectedCourseIds = new ArrayList<>();
        if (chkC1001.isSelected()) selectedCourseIds.add("C1001");
        if (chkC1002.isSelected()) selectedCourseIds.add("C1002");
        if (chkC1003.isSelected()) selectedCourseIds.add("C1003");
        if (chkC1004.isSelected()) selectedCourseIds.add("C1004");
        if (chkC1005.isSelected()) selectedCourseIds.add("C1005");

        if (studentId.isEmpty() || name.isEmpty() || selectedCourseIds.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All required fields must be filled out.").showAndWait();
            return;
        }

        StudentDTO studentDTO = new StudentDTO(studentId, name, contactNumber, email, LocalDate.now(), selectedCourseIds);

        try {
            boolean isRegistered = studentBO.registerStudent(studentDTO);
            if (isRegistered) {
                new Alert(Alert.AlertType.INFORMATION, "Student registered successfully!").showAndWait();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Registration failed!").showAndWait();
            }
        } catch (RegistrationException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtStudentId.clear();
        txtName.clear();
        txtEmail.clear();
        txtContactNumber.clear();
        chkC1001.setSelected(false);
        chkC1002.setSelected(false);
        chkC1003.setSelected(false);
        chkC1004.setSelected(false);
        chkC1005.setSelected(false);
    }
}
