package lk.ijse.elitedrivingschoolsystem.bo.custom.impl;

import lk.ijse.elitedrivingschoolsystem.bo.custom.CourseBO;
import lk.ijse.elitedrivingschoolsystem.bo.util.EntityDTOConverter;
import lk.ijse.elitedrivingschoolsystem.config.FactoryConfiguration;
import lk.ijse.elitedrivingschoolsystem.dao.DAOFactory;
import lk.ijse.elitedrivingschoolsystem.dao.DAOTypes;
import lk.ijse.elitedrivingschoolsystem.dao.custom.CourseDAO;
import lk.ijse.elitedrivingschoolsystem.dto.CourseDTO;
import lk.ijse.elitedrivingschoolsystem.entity.Course;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.stream.Collectors;

public class CourseBOImpl implements CourseBO {
    private final CourseDAO courseDAO = DAOFactory.getInstance().getDAO(DAOTypes.COURSE);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public boolean saveCourse(CourseDTO dto) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Course course = converter.getCourse(dto);
            boolean isSaved = courseDAO.save(course, session);
            if (isSaved) {
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
    public boolean updateCourse(CourseDTO dto) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Course course = converter.getCourse(dto);
            boolean isUpdated = courseDAO.update(course, session);
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
    public boolean deleteCourse(String courseId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            boolean isDeleted = courseDAO.delete(courseId, session);
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

    @Override
    public List<CourseDTO> getAllCourses() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            List<Course> courses = courseDAO.getAll(session);
            return courses.stream()
                    .map(converter::getCourseDTO)
                    .collect(Collectors.toList());
        } finally {
            session.close();
        }
    }

    // Implement the new method here
    @Override
    public CourseDTO getCourseByName(String courseName) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<Course> query = session.createQuery("FROM Course WHERE courseName = :name", Course.class);
            query.setParameter("name", courseName);
            Course course = query.uniqueResult();
            if (course != null) {
                return converter.getCourseDTO(course);
            }
            return null;
        } finally {
            session.close();
        }
    }
}