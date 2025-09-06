package lk.ijse.elitedrivingschoolsystem.bo.custom.impl;

import lk.ijse.elitedrivingschoolsystem.bo.custom.StudentBO;
import lk.ijse.elitedrivingschoolsystem.bo.exception.RegistrationException;
import lk.ijse.elitedrivingschoolsystem.bo.util.EntityDTOConverter;
import lk.ijse.elitedrivingschoolsystem.dao.DAOFactory;
import lk.ijse.elitedrivingschoolsystem.dao.DAOTypes;
import lk.ijse.elitedrivingschoolsystem.dao.custom.CourseDAO;
import lk.ijse.elitedrivingschoolsystem.dao.custom.StudentDAO;
import lk.ijse.elitedrivingschoolsystem.dto.StudentDTO;
import lk.ijse.elitedrivingschoolsystem.entity.Course;
import lk.ijse.elitedrivingschoolsystem.entity.Student;
import lk.ijse.elitedrivingschoolsystem.util.ValidationUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import lk.ijse.elitedrivingschoolsystem.config.FactoryConfiguration;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentBOImpl implements StudentBO {
    private final StudentDAO studentDAO = DAOFactory.getInstance().getDAO(DAOTypes.STUDENT);
    private final CourseDAO courseDAO = DAOFactory.getInstance().getDAO(DAOTypes.COURSE);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public boolean registerStudent(StudentDTO dto) throws RegistrationException {
        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new RegistrationException("Invalid email format.");
        }
        if (dto.getContactNumber() != null && !dto.getContactNumber().isEmpty() && !ValidationUtil.isValidPhoneNumber(dto.getContactNumber())) {
            throw new RegistrationException("Invalid phone number format.");
        }

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Student student = converter.getStudent(dto);

            // Fetch and set courses
            List<Course> courses = new ArrayList<>();
            for (String courseId : dto.getCourseIds()) {
                Course course = courseDAO.get(courseId, session);
                if (course != null) {
                    courses.add(course);
                }
            }
            student.setCourses(courses);

            boolean isSaved = studentDAO.save(student, session);
            if (isSaved) {
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RegistrationException("Student registration failed due to a system error.");
        } finally {
            session.close();
        }
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            List<Student> students = studentDAO.getAll(session);
            return students.stream()
                    .map(converter::getStudentDTO)
                    .collect(Collectors.toList());
        } finally {
            session.close();
        }
    }

    @Override
    public List<StudentDTO> getStudentsInAllCourses() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            List<Student> students = studentDAO.getStudentsInAllCourses(session);
            return students.stream()
                    .map(converter::getStudentDTO)
                    .collect(Collectors.toList());
        } finally {
            session.close();
        }
    }


    @Override
    public boolean updateStudent(StudentDTO dto) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Student student = converter.getStudent(dto);

            List<Course> courses = new ArrayList<>();
            for (String courseId : dto.getCourseIds()) {
                Course course = courseDAO.get(courseId, session);
                if (course != null) {
                    courses.add(course);
                }
            }
            student.setCourses(courses);

            boolean isUpdated = studentDAO.update(student, session);
            if (isUpdated) {
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteStudent(String studentId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            boolean isDeleted = studentDAO.delete(studentId, session);
            if (isDeleted) {
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

}
