package lk.ijse.elitedrivingschoolsystem.bo.custom;

import lk.ijse.elitedrivingschoolsystem.bo.SuperBO;
import lk.ijse.elitedrivingschoolsystem.dto.CourseDTO;
import lk.ijse.elitedrivingschoolsystem.dto.InstructorDTO;
import java.util.List;

public interface InstructorBO extends SuperBO {
    boolean saveInstructor(InstructorDTO dto);
    boolean updateInstructor(InstructorDTO dto);
    boolean deleteInstructor(String instructorId);
    List<InstructorDTO> getAllInstructors();

    // New methods for course assignment and tracking
    boolean assignCourseToInstructor(String instructorId, String courseName);
    List<CourseDTO> getAssignedCourses(String instructorId);

    // Add these new methods for saving and updating with courses
    boolean saveInstructorWithCourses(InstructorDTO instructor, List<CourseDTO> courses) throws Exception;
    boolean updateInstructorWithCourses(InstructorDTO instructor, List<CourseDTO> courses) throws Exception;
}