package lk.ijse.elitedrivingschoolsystem.bo.custom;

import lk.ijse.elitedrivingschoolsystem.bo.SuperBO;
import lk.ijse.elitedrivingschoolsystem.dto.InstructorDTO;

import java.util.List;

public interface InstructorBO extends SuperBO {
    boolean saveInstructor(InstructorDTO dto);
    boolean updateInstructor(InstructorDTO dto);
    boolean deleteInstructor(String instructorId);
    List<InstructorDTO> getAllInstructors();
}
