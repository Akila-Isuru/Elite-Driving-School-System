package lk.ijse.elitedrivingschoolsystem.bo.custom.impl;

import lk.ijse.elitedrivingschoolsystem.bo.custom.UserBO;
import lk.ijse.elitedrivingschoolsystem.bo.exception.LoginException;
import lk.ijse.elitedrivingschoolsystem.bo.exception.RegistrationException;
import lk.ijse.elitedrivingschoolsystem.bo.util.EntityDTOConverter;
import lk.ijse.elitedrivingschoolsystem.config.FactoryConfiguration;
import lk.ijse.elitedrivingschoolsystem.dao.DAOFactory;
import lk.ijse.elitedrivingschoolsystem.dao.DAOTypes;
import lk.ijse.elitedrivingschoolsystem.dao.custom.UserDAO;
import lk.ijse.elitedrivingschoolsystem.dto.UserDTO;
import lk.ijse.elitedrivingschoolsystem.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

public class UserBOImpl implements UserBO {
    private final UserDAO userDAO = DAOFactory.getInstance().getDAO(DAOTypes.USER);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public UserDTO loginUser(String username, String password) throws LoginException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            User user = userDAO.getUserByUsername(username, session);
            if (user == null || !user.checkPassword(password)) {
                throw new LoginException("Invalid username or password.");
            }
            return converter.getUserDTO(user);
        } catch (Exception e) {
            throw new LoginException("Login failed: " + e.getMessage());
        }
    }

    @Override
    public boolean registerUser(UserDTO dto) throws RegistrationException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            User existingUser = userDAO.getUserByUsername(dto.getUserName(), session);
            if (existingUser != null) {
                throw new RegistrationException("Username already exists.");
            }

            User user = converter.getUser(dto);
            // The User entity's @PrePersist/@PreUpdate method handles BCrypt hashing
            boolean isSaved = userDAO.save(user, session);
            if (isSaved) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            throw new RegistrationException("User registration failed: " + e.getMessage());
        }
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) throws LoginException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            User user = userDAO.getUserByUsername(username, session);
            if (user == null) {
                throw new LoginException("User not found.");
            }
            if (!user.checkPassword(oldPassword)) {
                throw new LoginException("Incorrect old password.");
            }

            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            boolean isUpdated = userDAO.update(user, session);

            if (isUpdated) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            throw new LoginException("Password change failed: " + e.getMessage());
        }
    }
}
