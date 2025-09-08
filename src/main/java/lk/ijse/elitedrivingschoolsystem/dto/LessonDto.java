package lk.ijse.elitedrivingschoolsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonDto {
    private String lessonId;
    private LocalDate date;
    private String time;
    private String StudentId; // Keep as String for simplicity when passing IDs
    private String InstructorId; // Keep as String for simplicity when passing IDs
    private String courseId;     // Keep as String for simplicity when passing IDs
}