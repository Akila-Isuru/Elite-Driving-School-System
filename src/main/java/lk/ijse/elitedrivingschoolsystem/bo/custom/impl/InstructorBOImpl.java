package lk.ijse.elitedrivingschoolsystem.bo.custom.impl;

import lk.ijse.elitedrivingschoolsystem.bo.custom.InstructorBO;
import lk.ijse.elitedrivingschoolsystem.bo.util.EntityDTOConverter;
import lk.ijse.elitedrivingschoolsystem.config.FactoryConfiguration;
import lk.ijse.elitedrivingschoolsystem.dao.DAOFactory;
import lk.ijse.elitedrivingschoolsystem.dao.DAOTypes;
import lk.ijse.elitedrivingschoolsystem.dao.custom.InstructorDAO;
import lk.ijse.elitedrivingschoolsystem.dto.InstructorDTO;
import lk.ijse.elitedrivingschoolsystem.entity.Instructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class InstructorBOImpl implements InstructorBO {
    private final InstructorDAO instructorDAO = DAOFactory.getInstance().getDAO(DAOTypes.INSTRUCTOR);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public boolean saveInstructor(InstructorDTO dto) {
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

    @Override
    public boolean updateInstructor(InstructorDTO dto) {
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
}
