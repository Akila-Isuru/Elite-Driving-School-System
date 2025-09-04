package lk.ijse.elitedrivingschoolsystem.dao;

import org.hibernate.Session;

import java.util.List;

public interface CrudDAO<T, ID> extends SuperDAO {

    boolean save(T entity, Session session);


    boolean update(T entity, Session session);

    boolean delete(ID id, Session session);


    T get(ID id, Session session);


    List<T> getAll(Session session);
}
