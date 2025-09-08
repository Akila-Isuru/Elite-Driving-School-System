package lk.ijse.elitedrivingschoolsystem.bo.custom.impl;

import lk.ijse.elitedrivingschoolsystem.bo.custom.InstructorBO;
import lk.ijse.elitedrivingschoolsystem.bo.util.EntityDTOConverter;
import lk.ijse.elitedrivingschoolsystem.config.FactoryConfiguration;
import lk.ijse.elitedrivingschoolsystem.dao.DAOFactory;
import lk.ijse.elitedrivingschoolsystem.dao.DAOTypes;
import lk.ijse.elitedrivingschoolsystem.dao.custom.CourseDAO;
import lk.ijse.elitedrivingschoolsystem.dao.custom.InstructorDAO;
import lk.ijse.elitedrivingschoolsystem.dto.CourseDTO;
import lk.ijse.elitedrivingschoolsystem.dto.InstructorDTO;
import lk.ijse.elitedrivingschoolsystem.entity.Course;
import lk.ijse.elitedrivingschoolsystem.entity.Instructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InstructorBOImpl implements InstructorBO {
    private final InstructorDAO instructorDAO = DAOFactory.getInstance().getDAO(DAOTypes.INSTRUCTOR);
    private final CourseDAO courseDAO = DAOFactory.getInstance().getDAO(DAOTypes.COURSE);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public boolean saveInstructor(InstructorDTO dto) {
        // This method can be kept for simple saves, but the new one is better for the UI flow.
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Instructor instructor = converter.getInstructor(dto);
            boolean isSaved = instructorDAO.save(instructor, session);
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

    // New method implementation
    @Override
    public boolean saveInstructorWithCourses(InstructorDTO dto, List<CourseDTO> courseDTOs) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Instructor instructor = converter.getInstructor(dto);

            // Convert CourseDTOs to Course entities and link them to the instructor
            for (CourseDTO courseDTO : courseDTOs) {
                Course course = courseDAO.get(courseDTO.getCourseId(), session);
                if (course != null) {
                    instructor.getCourses().add(course);
                    course.getInstructors().add(instructor);
                }
            }

            boolean isSaved = instructorDAO.save(instructor, session);
            if (isSaved) {
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new Exception("Error saving instructor and courses", e);
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updateInstructor(InstructorDTO dto) {
        // This can also be kept, but the new one handles courses better.
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Instructor instructor = converter.getInstructor(dto);
            boolean isUpdated = instructorDAO.update(instructor, session);
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

    // New method implementation
    @Override
    public boolean updateInstructorWithCourses(InstructorDTO dto, List<CourseDTO> courseDTOs) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Instructor instructor = instructorDAO.get(dto.getInstructorId(), session);
            if (instructor == null) {
                transaction.rollback();
                return false;
            }

            // Update instructor details
            instructor.setName(dto.getName());
            instructor.setContactNumber(dto.getContactNumber());
            instructor.setEmail(dto.getEmail());

            // Clear existing courses to replace with new list
            instructor.getCourses().clear();

            // Add new courses
            for (CourseDTO courseDTO : courseDTOs) {
                Course course = courseDAO.get(courseDTO.getCourseId(), session);
                if (course != null) {
                    instructor.getCourses().add(course);
                }
            }

            session.merge(instructor);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new Exception("Error updating instructor and courses", e);
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteInstructor(String instructorId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            boolean isDeleted = instructorDAO.delete(instructorId, session);
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
    public List<InstructorDTO> getAllInstructors() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            List<Instructor> instructors = instructorDAO.getAll(session);
            return instructors.stream()
                    .map(converter::getInstructorDTO)
                    .collect(Collectors.toList());
        } finally {
            session.close();
        }
    }

    @Override
    public boolean assignCourseToInstructor(String instructorId, String courseName) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Instructor instructor = session.get(Instructor.class, instructorId);
            Query<Course> query = session.createQuery("FROM Course WHERE courseName = :name", Course.class);
            query.setParameter("name", courseName);
            Course course = query.uniqueResult();
            if (instructor != null && course != null) {
                // Check if the course is already assigned to avoid duplicates
                if (instructor.getCourses().contains(course)) {
                    transaction.rollback();
                    return false;
                }
                instructor.getCourses().add(course);
                course.getInstructors().add(instructor);
                session.merge(instructor);
                session.merge(course);
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
    public List<CourseDTO> getAssignedCourses(String instructorId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Instructor instructor = instructorDAO.get(instructorId, session);
            if (instructor != null && instructor.getCourses() != null) {
                return instructor.getCourses().stream()
                        .map(converter::getCourseDTO)
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
}