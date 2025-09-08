package lk.ijse.elitedrivingschoolsystem.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.elitedrivingschoolsystem.bo.BOFactory;
import lk.ijse.elitedrivingschoolsystem.bo.BOTypes;
import lk.ijse.elitedrivingschoolsystem.bo.custom.CourseBO;
import lk.ijse.elitedrivingschoolsystem.bo.custom.InstructorBO;
import lk.ijse.elitedrivingschoolsystem.bo.custom.LessonBO;
import lk.ijse.elitedrivingschoolsystem.dto.CourseDTO;
import lk.ijse.elitedrivingschoolsystem.dto.InstructorDTO;
import lk.ijse.elitedrivingschoolsystem.dto.LessonDto;
import lk.ijse.elitedrivingschoolsystem.util.ValidationUtil;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class InstructorRegistrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtInstructorId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtContactNumber;

    @FXML
    private TableView<InstructorDTO> tblInstructors;

    @FXML
    private TableColumn<InstructorDTO, String> colId;

    @FXML
    private TableColumn<InstructorDTO, String> colName;

    @FXML
    private TableColumn<InstructorDTO, String> colContact;

    @FXML
    private TableColumn<InstructorDTO, String> colEmail;

    @FXML
    private ComboBox<String> cmbCourses;

    @FXML
    private TableView<CourseDTO> tblAssignedCourses;

    @FXML
    private TableColumn<CourseDTO, String> colCourseId;

    @FXML
    private TableColumn<CourseDTO, String> colCourseName;

    @FXML
    private TableColumn<CourseDTO, String> colCourseDuration;

    @FXML
    private TableColumn<CourseDTO, String> colCourseFee;

    @FXML
    private TableView<LessonDto> tblSchedule;

    @FXML
    private TableColumn<LessonDto, String> colLessonId;

    @FXML
    private TableColumn<LessonDto, String> colLessonDate;

    @FXML
    private TableColumn<LessonDto, String> colLessonTime;

    @FXML
    private TableColumn<LessonDto, String> colLessonStudent;

    private InstructorBO instructorBO;
    private CourseBO courseBO;
    private LessonBO lessonBO;

    @FXML
    void initialize() {
        // Initialize BO instances here
        instructorBO = (InstructorBO) BOFactory.getInstance().getBO(BOTypes.INSTRUCTOR);
        courseBO = (CourseBO) BOFactory.getInstance().getBO(BOTypes.COURSE);
        lessonBO = (LessonBO) BOFactory.getInstance().getBO(BOTypes.LESSON);

        setMainTableCells();
        setAssignedCoursesTableCells();
        setScheduleTableCells();
        loadAllInstructors();
        loadAllCourses(); // This will now correctly load courses into the ComboBox

        tblInstructors.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFields(newSelection);
                loadAssignedCourses(newSelection.getInstructorId());
                loadInstructorSchedule(newSelection.getInstructorId());
            } else {
                clearFields();
                tblAssignedCourses.getItems().clear();
                tblSchedule.getItems().clear();
            }
        });

        // Listener for ComboBox selection to automatically add to the table
        cmbCourses.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    // Fetch the full CourseDTO using the selected course name
                    CourseDTO selectedCourse = courseBO.getCourseByName(newSelection);
                    if (selectedCourse != null) {
                        addCourseToAssignedTable(selectedCourse);
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve course details.");
                    e.printStackTrace();
                }
            }
        });
    }

    private void setMainTableCells() {
        colId.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void setAssignedCoursesTableCells() {
        colCourseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colCourseDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colCourseFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
    }

    private void setScheduleTableCells() {
        colLessonId.setCellValueFactory(new PropertyValueFactory<>("lessonId"));
        colLessonDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colLessonTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colLessonStudent.setCellValueFactory(new PropertyValueFactory<>("studentId"));
    }

    private void loadAllInstructors() {
        try {
            List<InstructorDTO> instructorList = instructorBO.getAllInstructors();
            ObservableList<InstructorDTO> observableList = FXCollections.observableArrayList(instructorList);
            tblInstructors.setItems(observableList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load instructors from the database. See console for details.");
            e.printStackTrace();
        }
    }

    // This method is now corrected to load course names into the ComboBox
    private void loadAllCourses() {
        try {
            List<CourseDTO> courses = courseBO.getAllCourses();
            // Extract just the course names to display in the ComboBox
            ObservableList<String> courseNames = FXCollections.observableArrayList(
                    courses.stream().map(CourseDTO::getCourseName).collect(Collectors.toList())
            );
            cmbCourses.setItems(courseNames);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load courses. See console for details.");
            e.printStackTrace();
        }
    }

    // This method adds the selected course to the table visually
    private void addCourseToAssignedTable(CourseDTO course) {
        ObservableList<CourseDTO> assignedCourses = tblAssignedCourses.getItems();
        if (assignedCourses == null) {
            assignedCourses = FXCollections.observableArrayList();
            tblAssignedCourses.setItems(assignedCourses);
        }

        // Check if the course is already in the table to prevent duplicates
        boolean isAlreadyAssigned = assignedCourses.stream()
                .anyMatch(c -> c.getCourseId().equals(course.getCourseId()));

        if (!isAlreadyAssigned) {
            assignedCourses.add(course);
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "This course is already in the assigned list.");
        }
    }


    private void loadAssignedCourses(String instructorId) {
        try {
            List<CourseDTO> assignedCourses = instructorBO.getAssignedCourses(instructorId);
            ObservableList<CourseDTO> observableList = FXCollections.observableArrayList(assignedCourses);
            tblAssignedCourses.setItems(observableList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load assigned courses. See console for details.");
            e.printStackTrace();
        }
    }

    private void loadInstructorSchedule(String instructorId) {
        try {
            List<LessonDto> schedule = lessonBO.getLessonsByInstructorId(instructorId);
            ObservableList<LessonDto> observableList = FXCollections.observableArrayList(schedule);
            tblSchedule.setItems(observableList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load instructor's schedule. See console for details.");
            e.printStackTrace();
        }
    }

    private void fillFields(InstructorDTO instructor) {
        txtInstructorId.setText(instructor.getInstructorId());
        txtName.setText(instructor.getName());
        txtContactNumber.setText(instructor.getContactNumber());
        txtEmail.setText(instructor.getEmail());
        txtInstructorId.setDisable(true);
    }

    private void clearFields() {
        txtInstructorId.clear();
        txtName.clear();
        txtContactNumber.clear();
        txtEmail.clear();
        txtInstructorId.setDisable(false);
        tblInstructors.getSelectionModel().clearSelection();
        tblAssignedCourses.getItems().clear();
        tblSchedule.getItems().clear();
        cmbCourses.getSelectionModel().clearSelection();
    }

    private boolean validateInputs(boolean isSave) {
        String instructorId = txtInstructorId.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String contactNumber = txtContactNumber.getText();

        if (isSave && instructorId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Instructor ID cannot be empty.");
            return false;
        }
        if (name.isEmpty() || email.isEmpty() || contactNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return false;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format.");
            return false;
        }
        if (!ValidationUtil.isValidPhoneNumber(contactNumber)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid contact number. It must be 10 digits.");
            return false;
        }
        return true;
    }

    @FXML
    void handleSaveButton(ActionEvent event) {
        if (!validateInputs(true)) {
            return;
        }
        String instructorId = txtInstructorId.getText();
        String name = txtName.getText();
        String contactNumber = txtContactNumber.getText();
        String email = txtEmail.getText();
        InstructorDTO newInstructor = new InstructorDTO(instructorId, name, contactNumber, email);

        // Get the list of courses from the assigned courses table
        List<CourseDTO> assignedCourses = tblAssignedCourses.getItems();

        try {
            boolean isSaved = instructorBO.saveInstructorWithCourses(newInstructor, assignedCourses);
            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Instructor saved successfully!");
                clearFields();
                loadAllInstructors();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save the instructor. ID might already exist.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while saving.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateButton(ActionEvent event) {
        if (!validateInputs(false)) {
            return;
        }
        InstructorDTO selectedInstructor = tblInstructors.getSelectionModel().getSelectedItem();
        if (selectedInstructor == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an instructor to update.");
            return;
        }
        selectedInstructor.setName(txtName.getText());
        selectedInstructor.setContactNumber(txtContactNumber.getText());
        selectedInstructor.setEmail(txtEmail.getText());

        // Get the updated list of courses from the assigned courses table
        List<CourseDTO> updatedCourses = tblAssignedCourses.getItems();
        try {
            boolean isUpdated = instructorBO.updateInstructorWithCourses(selectedInstructor, updatedCourses);
            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Instructor updated successfully!");
                clearFields();
                loadAllInstructors();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update the instructor.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while updating.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteButton(ActionEvent event) {
        InstructorDTO selectedInstructor = tblInstructors.getSelectionModel().getSelectedItem();
        if (selectedInstructor == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an instructor to delete.");
            return;
        }
        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this instructor?").showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = instructorBO.deleteInstructor(selectedInstructor.getInstructorId());
                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Instructor deleted successfully!");
                    clearFields();
                    loadAllInstructors();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the instructor.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while deleting.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleClearButton(ActionEvent event) {
        clearFields();
    }

    @FXML
    void handleAssignButton(ActionEvent event) {
        // This method is no longer strictly necessary if using the listener,
        // but can be kept for manual assignment.
        String selectedCourseName = cmbCourses.getSelectionModel().getSelectedItem();
        if (selectedCourseName == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a course to assign.");
            return;
        }
        try {
            CourseDTO selectedCourse = courseBO.getCourseByName(selectedCourseName);
            if (selectedCourse != null) {
                addCourseToAssignedTable(selectedCourse);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve course details.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        new Alert(type, message).showAndWait();
    }
}