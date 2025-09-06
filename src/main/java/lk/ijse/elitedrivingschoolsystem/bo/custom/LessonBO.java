package lk.ijse.elitedrivingschoolsystem.bo.custom;

import lk.ijse.elitedrivingschoolsystem.bo.SuperBO;
import lk.ijse.elitedrivingschoolsystem.bo.exception.LessonException;
import lk.ijse.elitedrivingschoolsystem.dto.LessonDto;

import java.util.List;

public interface LessonBO extends SuperBO {
    boolean scheduleLesson(LessonDto dto) throws LessonException;
    boolean updateLesson(LessonDto dto) throws LessonException;
    boolean deleteLesson(String lessonId);
    List<LessonDto> getAllLessons();
}
