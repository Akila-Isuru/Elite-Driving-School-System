package lk.ijse.elitedrivingschoolsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentDTO {
    private String studentId;
    private String name;
    private String contactNumber;
    private String email;
    private LocalDate regDate;
    private List<String> courseIds;
}
