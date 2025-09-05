package lk.ijse.elitedrivingschoolsystem.dao.custom.impl;

import lk.ijse.elitedrivingschoolsystem.dao.custom.InstructorDAO;
import lk.ijse.elitedrivingschoolsystem.entity.Instructor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class InstructorDAOImpl implements InstructorDAO {
    @Override
    public boolean save(Instructor entity, Session session) {
        try {
            session.persist(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Instructor entity, Session session) {
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
            Instructor instructor = session.get(Instructor.class, id);
            if (instructor != null) {
                session.remove(instructor);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Instructor get(String id, Session session) {
        return session.get(Instructor.class, id);
    }

    @Override
    public List<Instructor> getAll(Session session) {
        Query<Instructor> query = session.createQuery("FROM Instructor", Instructor.class);
        return query.list();
    }
}
