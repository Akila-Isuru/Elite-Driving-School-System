package lk.ijse.elitedrivingschoolsystem.bo.custom.impl;

import lk.ijse.elitedrivingschoolsystem.bo.custom.LessonBO;
import lk.ijse.elitedrivingschoolsystem.bo.exception.LessonException;
import lk.ijse.elitedrivingschoolsystem.bo.util.EntityDTOConverter;
import lk.ijse.elitedrivingschoolsystem.config.FactoryConfiguration;
import lk.ijse.elitedrivingschoolsystem.dao.DAOFactory;
import lk.ijse.elitedrivingschoolsystem.dao.DAOTypes;
import lk.ijse.elitedrivingschoolsystem.dao.custom.CourseDAO;
import lk.ijse.elitedrivingschoolsystem.dao.custom.InstructorDAO;
import lk.ijse.elitedrivingschoolsystem.dao.custom.LessonDAO;
import lk.ijse.elitedrivingschoolsystem.dao.custom.StudentDAO;
import lk.ijse.elitedrivingschoolsystem.dto.LessonDto;
import lk.ijse.elitedrivingschoolsystem.entity.Course;
import lk.ijse.elitedrivingschoolsystem.entity.Instructor;
import lk.ijse.elitedrivingschoolsystem.entity.Lesson;
import lk.ijse.elitedrivingschoolsystem.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.stream.Collectors;

public class LessonBOImpl implements LessonBO {
    private final LessonDAO lessonDAO = DAOFactory.getInstance().getDAO(DAOTypes.LESSON);
    private final StudentDAO studentDAO = DAOFactory.getInstance().getDAO(DAOTypes.STUDENT);
    private final InstructorDAO instructorDAO = DAOFactory.getInstance().getDAO(DAOTypes.INSTRUCTOR);
    private final CourseDAO courseDAO = DAOFactory.getInstance().getDAO(DAOTypes.COURSE);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public boolean scheduleLesson(LessonDto dto) throws LessonException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // Check for scheduling conflicts
            List<Lesson> existingLessons = lessonDAO.getAll(session);
            for (Lesson lesson : existingLessons) {
                if (lesson.getInstructor().getInstructorId().equals(dto.getInstructorId()) &&
                        lesson.getDate().equals(dto.getDate()) &&
                        lesson.getTime().equals(dto.getTime())) {
                    throw new LessonException("Instructor is already booked for a lesson at this time.");
                }
            }

            // Fetch and set entities
            Student student = studentDAO.get(dto.getStudentId(), session);
            Instructor instructor = instructorDAO.get(dto.getInstructorId(), session);
            Course course = courseDAO.get(dto.getCourseId(), session);

            if (student == null || instructor == null || course == null) {
                throw new LessonException("Invalid Student, Instructor, or Course ID.");
            }

            Lesson lesson = converter.getLesson(dto);
            lesson.setStudent(student);
            lesson.setInstructor(instructor);
            lesson.setCourse(course);

            boolean isSaved = lessonDAO.save(lesson, session);
            if (isSaved) {
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new LessonException("Lesson scheduling failed: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updateLesson(LessonDto dto) throws LessonException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            Student student = studentDAO.get(dto.getStudentId(), session);
            Instructor instructor = instructorDAO.get(dto.getInstructorId(), session);
            Course course = courseDAO.get(dto.getCourseId(), session);

            if (student == null || instructor == null || course == null) {
                throw new LessonException("Invalid Student, Instructor, or Course ID.");
            }

            Lesson lesson = converter.getLesson(dto);
            lesson.setStudent(student);
            lesson.setInstructor(instructor);
            lesson.setCourse(course);

            boolean isUpdated = lessonDAO.update(lesson, session);
            if (isUpdated) {
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new LessonException("Lesson update failed: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteLesson(String lessonId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            boolean isDeleted = lessonDAO.delete(lessonId, session);
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
    public List<LessonDto> getAllLessons() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            List<Lesson> lessons = lessonDAO.getAll(session);
            return lessons.stream()
                    .map(converter::getLessonDTO)
                    .collect(Collectors.toList());
        } finally {
            session.close();
        }
    }

    @Override
    public List<LessonDto> getLessonsByInstructorId(String instructorId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            // Use a Hibernate query to fetch lessons for a specific instructor
            Query<Lesson> query = session.createQuery("FROM Lesson WHERE instructor.instructorId = :instructorId", Lesson.class);
            query.setParameter("instructorId", instructorId);
            List<Lesson> lessons = query.list();
            return lessons.stream()
                    .map(converter::getLessonDTO)
                    .collect(Collectors.toList());
        } finally {
            session.close();
        }
    }
}
