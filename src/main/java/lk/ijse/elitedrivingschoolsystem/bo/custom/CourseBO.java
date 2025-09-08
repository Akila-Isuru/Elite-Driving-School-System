package lk.ijse.elitedrivingschoolsystem.bo.custom;

import lk.ijse.elitedrivingschoolsystem.bo.SuperBO;
import lk.ijse.elitedrivingschoolsystem.dto.CourseDTO;

import java.util.List;

public interface CourseBO extends SuperBO {
    boolean saveCourse(CourseDTO dto);
    boolean updateCourse(CourseDTO dto);
    boolean deleteCourse(String courseId);
    List<CourseDTO> getAllCourses();

    // Add this new method
    CourseDTO getCourseByName(String courseName) throws Exception;
}