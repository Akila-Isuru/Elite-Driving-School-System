package lk.ijse.elitedrivingschoolsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CourseDTO {
    private String CourseId;
    private String CourseName;
    private String duration;
    private String fee;
}
