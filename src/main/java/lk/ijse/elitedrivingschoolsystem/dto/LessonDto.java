package lk.ijse.elitedrivingschoolsystem.dto;

import com.ctc.wstx.evt.WstxEventReader;
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
    private String StudentId;
    private String InstructorId;
    private String courseId;

}
