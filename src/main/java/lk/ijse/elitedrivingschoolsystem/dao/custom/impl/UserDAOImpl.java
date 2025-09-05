package lk.ijse.elitedrivingschoolsystem.dao.custom.impl;

import lk.ijse.elitedrivingschoolsystem.dao.custom.UserDAO;
import lk.ijse.elitedrivingschoolsystem.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserDAOImpl implements UserDAO {
    @Override
    public boolean save(User entity, Session session) {
        try {
            session.persist(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(User entity, Session session) {
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
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User get(String id, Session session) {
        return session.get(User.class, id);
    }

    @Override
    public List<User> getAll(Session session) {
        Query<User> query = session.createQuery("FROM User", User.class);
        return query.list();
    }

    @Override
    public User getUserByUsername(String username, Session session) {
        try {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
