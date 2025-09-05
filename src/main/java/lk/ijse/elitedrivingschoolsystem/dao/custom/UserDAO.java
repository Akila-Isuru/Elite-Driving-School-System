package lk.ijse.elitedrivingschoolsystem.dao.custom;

import lk.ijse.elitedrivingschoolsystem.dao.CrudDAO;
import lk.ijse.elitedrivingschoolsystem.entity.User;
import org.hibernate.Session;

public interface UserDAO extends CrudDAO<User, String> {
    User getUserByUsername(String username, Session session);
}
