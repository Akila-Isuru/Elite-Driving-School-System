package lk.ijse.elitedrivingschoolsystem.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class StudentRegistrationController implements Initializable {

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

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private TableView<StudentDTO> tblStudents;

    @FXML
    private TableColumn<StudentDTO, String> colStudentId;

    @FXML
    private TableColumn<StudentDTO, String> colName;

    @FXML
    private TableColumn<StudentDTO, String> colEmail;

    @FXML
    private TableColumn<StudentDTO, String> colContact;

    @FXML
    private TableColumn<StudentDTO, String> colCourses; // This column will display the course IDs


    private final StudentBO studentBO = (StudentBO) BOFactory.getInstance().getBO(BOTypes.STUDENT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableColumnProperties();
        loadAllStudents();

        tblStudents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithStudentData(newSelection);
            }
        });
    }

    private void setTableColumnProperties() {
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        // For the 'Courses' column, we need a custom cell value factory because it's a List<String>
        colCourses.setCellValueFactory(data -> {
            StudentDTO student = data.getValue();
            if (student != null && student.getCourseIds() != null) {
                // Join the list of course IDs into a single string, separated by commas
                String courses = String.join(", ", student.getCourseIds());
                return new javafx.beans.property.SimpleStringProperty(courses);
            } else {
                return new javafx.beans.property.SimpleStringProperty(""); // Return empty string if no courses
            }
        });
    }

    private void loadAllStudents() {
        try {
            // Use the updated method to get StudentDTOs
            List<StudentDTO> students = studentBO.getAllStudentDTOs();
            ObservableList<StudentDTO> studentObservableList = FXCollections.observableArrayList(students);
            tblStudents.setItems(studentObservableList);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load students: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    private void fillFormWithStudentData(StudentDTO student) {
        txtStudentId.setText(student.getStudentId());
        txtName.setText(student.getName());
        txtEmail.setText(student.getEmail());
        txtContactNumber.setText(student.getContactNumber());
        clearCourseCheckBoxes();
        if (student.getCourseIds() != null) {
            student.getCourseIds().forEach(courseId -> {
                switch (courseId) {
                    case "C1001": chkC1001.setSelected(true); break;
                    case "C1002": chkC1002.setSelected(true); break;
                    case "C1003": chkC1003.setSelected(true); break;
                    case "C1004": chkC1004.setSelected(true); break;
                    case "C1005": chkC1005.setSelected(true); break;
                }
            });
        }
    }

    private void clearCourseCheckBoxes() {
        chkC1001.setSelected(false);
        chkC1002.setSelected(false);
        chkC1003.setSelected(false);
        chkC1004.setSelected(false);
        chkC1005.setSelected(false);
    }

    private List<String> getSelectedCourseIds() {
        List<String> selectedCourseIds = new ArrayList<>();
        if (chkC1001.isSelected()) selectedCourseIds.add("C1001");
        if (chkC1002.isSelected()) selectedCourseIds.add("C1002");
        if (chkC1003.isSelected()) selectedCourseIds.add("C1003");
        if (chkC1004.isSelected()) selectedCourseIds.add("C1004");
        if (chkC1005.isSelected()) selectedCourseIds.add("C1005");
        return selectedCourseIds;
    }

    @FXML
    void handleSaveButton(ActionEvent event) {
        String studentId = txtStudentId.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String contactNumber = txtContactNumber.getText();
        List<String> selectedCourseIds = getSelectedCourseIds();

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
                loadAllStudents(); // Refresh table
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

    @FXML
    void handleUpdateButton(ActionEvent event) {
        String studentId = txtStudentId.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String contactNumber = txtContactNumber.getText();
        List<String> selectedCourseIds = getSelectedCourseIds();

        if (studentId.isEmpty() || name.isEmpty() || selectedCourseIds.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select a student and fill out all required fields.").showAndWait();
            return;
        }

        StudentDTO studentDTO = new StudentDTO(studentId, name, contactNumber, email, LocalDate.now(), selectedCourseIds);

        try {
            boolean isUpdated = studentBO.updateStudent(studentDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Student updated successfully!").showAndWait();
                clearFields();
                loadAllStudents(); // Refresh table
            } else {
                new Alert(Alert.AlertType.ERROR, "Update failed!").showAndWait();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteButton(ActionEvent event) {
        String studentId = txtStudentId.getText();
        if (studentId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select the student you want to delete.").showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this student?", ButtonType.YES, ButtonType.NO);
        confirmAlert.showAndWait();

        if (confirmAlert.getResult() == ButtonType.YES) {
            try {
                boolean isDeleted = studentBO.deleteStudent(studentId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Student deleted successfully!").showAndWait();
                    clearFields();
                    loadAllStudents(); // Refresh table
                } else {
                    new Alert(Alert.AlertType.ERROR, "Deletion failed!").showAndWait();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        txtStudentId.clear();
        txtName.clear();
        txtEmail.clear();
        txtContactNumber.clear();
        clearCourseCheckBoxes();
    }
}