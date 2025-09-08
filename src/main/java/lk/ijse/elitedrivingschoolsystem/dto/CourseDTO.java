package lk.ijse.elitedrivingschoolsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDTO {
    private String CourseId; // Use consistent casing (e.g., courseId)
    private String CourseName; // Use consistent casing (e.g., courseName)
    private String duration;
    private String fee;
}