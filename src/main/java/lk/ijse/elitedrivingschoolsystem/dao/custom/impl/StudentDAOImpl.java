package lk.ijse.elitedrivingschoolsystem.dao.custom.impl;

import lk.ijse.elitedrivingschoolsystem.bo.util.EntityDTOConverter;
import lk.ijse.elitedrivingschoolsystem.dao.custom.StudentDAO;
import lk.ijse.elitedrivingschoolsystem.dto.StudentDTO;
import lk.ijse.elitedrivingschoolsystem.entity.Course; // Import Course class
import lk.ijse.elitedrivingschoolsystem.entity.Student;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.stream.Collectors;

public class StudentDAOImpl implements StudentDAO {

    private final EntityDTOConverter converter = new EntityDTOConverter(); // Need this to convert entities to DTOs

    @Override
    public boolean save(Student entity, Session session) {
        try {
            // Save student first
            session.persist(entity);

            // Manually persist courses if cascade is not working as expected
            if (entity.getCourses() != null) {
                for (Course course : entity.getCourses()) {
                    // Ensure course is managed by the session before persisting
                    if (!session.contains(course)) {
                        session.persist(course);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Student entity, Session session) {
        try {
            session.merge(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id, Session session) {
        try {
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.remove(student);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Student get(String id, Session session) {
        return session.get(Student.class, id);
    }

    @Override
    public List<Student> getAll(Session session) {
        Query<Student> query = session.createQuery("FROM Student", Student.class);
        return query.list();
    }

    // Added method to return List<StudentDTO>
    public List<StudentDTO> getAllStudentDTOs(Session session) {
        Query<Student> query = session.createQuery("FROM Student", Student.class);
        List<Student> students = query.list();
        return students.stream()
                .map(converter::getStudentDTO) // Using the converter
                .collect(Collectors.toList());
    }


    @Override
    public List<Student> getStudentsInAllCourses(Session session) {
        String hql = "SELECT s FROM Student s " +
                "WHERE SIZE(s.courses) = (SELECT COUNT(c.courseId) FROM Course c)";
        Query<Student> query = session.createQuery(hql, Student.class);
        return query.list();
    }
}