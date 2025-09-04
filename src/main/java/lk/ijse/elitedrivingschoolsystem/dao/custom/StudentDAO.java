package lk.ijse.elitedrivingschoolsystem.dao.custom;

import lk.ijse.elitedrivingschoolsystem.dao.CrudDAO;
import lk.ijse.elitedrivingschoolsystem.entity.Student;
import org.hibernate.Session;
import java.util.List;

public interface StudentDAO extends CrudDAO<Student, String> {
    List<Student> getStudentsInAllCourses(Session session);
}
