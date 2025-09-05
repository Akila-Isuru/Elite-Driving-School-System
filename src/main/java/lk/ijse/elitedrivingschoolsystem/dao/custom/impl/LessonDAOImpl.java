package lk.ijse.elitedrivingschoolsystem.dao.custom.impl;

import lk.ijse.elitedrivingschoolsystem.dao.custom.LessonDAO;
import lk.ijse.elitedrivingschoolsystem.entity.Lesson;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class LessonDAOImpl implements LessonDAO {
    @Override
    public boolean save(Lesson entity, Session session) {
        try {
            session.persist(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Lesson entity, Session session) {
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
            Lesson lesson = session.get(Lesson.class, id);
            if (lesson != null) {
                session.remove(lesson);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Lesson get(String id, Session session) {
        return session.get(Lesson.class, id);
    }

    @Override
    public List<Lesson> getAll(Session session) {
        Query<Lesson> query = session.createQuery("FROM Lesson", Lesson.class);
        return query.list();
    }
}
