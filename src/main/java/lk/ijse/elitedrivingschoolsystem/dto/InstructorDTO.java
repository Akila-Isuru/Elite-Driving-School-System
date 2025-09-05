package lk.ijse.elitedrivingschoolsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class InstructorDTO {
    private String instructorId;
    private String name;
    private String contactNumber;
    private String email;

}
