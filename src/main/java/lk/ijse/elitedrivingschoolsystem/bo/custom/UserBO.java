package lk.ijse.elitedrivingschoolsystem.bo.custom;

import lk.ijse.elitedrivingschoolsystem.bo.SuperBO;
import lk.ijse.elitedrivingschoolsystem.bo.exception.LoginException;
import lk.ijse.elitedrivingschoolsystem.bo.exception.RegistrationException;
import lk.ijse.elitedrivingschoolsystem.dto.UserDTO;

public interface UserBO extends SuperBO {
    UserDTO loginUser(String username, String password) throws LoginException;
    boolean registerUser(UserDTO dto) throws RegistrationException;
    boolean changePassword(String username, String oldPassword, String newPassword) throws LoginException;
}