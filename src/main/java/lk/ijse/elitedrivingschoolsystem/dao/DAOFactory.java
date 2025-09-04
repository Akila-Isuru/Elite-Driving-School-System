package lk.ijse.elitedrivingschoolsystem.dao;

import lk.ijse.elitedrivingschoolsystem.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return daoFactory == null ? (daoFactory = new DAOFactory()) : daoFactory;
    }

    @SuppressWarnings("unchecked")
    public <T extends SuperDAO> T getDAO(DAOTypes daoType) {
        switch (daoType) {
            case STUDENT:
                return (T) new StudentDAOImpl();
            case COURSE:
                return (T) new CourseDAOImpl();
            case INSTRUCTOR:
                return (T) new InstructorDAOImpl();
            case LESSON:
                return (T) new LessonDAOImpl();
            case PAYMENT:
                return (T) new PaymentDAOImpl();
            case USER:
                return (T) new UserDAOImpl();
            default:
                return null;
        }
    }
}
