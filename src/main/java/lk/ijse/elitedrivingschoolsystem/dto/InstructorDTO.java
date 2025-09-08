package lk.ijse.elitedrivingschoolsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class InstructorDTO {
    private String instructorId;
    private String name;
    private String contactNumber;
    private String email;
    private List<CourseDTO> courses = new ArrayList<>(); // For assigned courses
    private List<LessonDto> lessons = new ArrayList<>(); // For schedule

    // Constructor for basic details, as used in the form
    public InstructorDTO(String instructorId, String name, String contactNumber, String email) {
        this.instructorId = instructorId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
    }
}