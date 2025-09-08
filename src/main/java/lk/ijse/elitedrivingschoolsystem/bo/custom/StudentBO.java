package lk.ijse.elitedrivingschoolsystem.bo.custom;

import lk.ijse.elitedrivingschoolsystem.bo.SuperBO;
import lk.ijse.elitedrivingschoolsystem.bo.exception.RegistrationException;
import lk.ijse.elitedrivingschoolsystem.dto.StudentDTO;

import java.util.List;

public interface StudentBO extends SuperBO {
    boolean registerStudent(StudentDTO dto) throws RegistrationException;
    List<StudentDTO> getAllStudentDTOs(); // Changed method name
    List<StudentDTO> getStudentsInAllCourses();
    boolean updateStudent(StudentDTO dto);
    boolean deleteStudent(String studentId);
}