
package lk.ijse.elitedrivingschoolsystem.bo;

//import lk.ijse.elitedrivingschoolsystem.bo.custom.impl.CourseBOImpl;
//import lk.ijse.elitedrivingschoolsystem.bo.custom.impl.InstructorBOImpl;
//import lk.ijse.elitedrivingschoolsystem.bo.custom.impl.LessonBOImpl;
//import lk.ijse.elitedrivingschoolsystem.bo.custom.impl.PaymentBOImpl;
import lk.ijse.elitedrivingschoolsystem.bo.custom.impl.StudentBOImpl;
import lk.ijse.elitedrivingschoolsystem.bo.custom.impl.UserBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {
        STUDENT, COURSE, INSTRUCTOR, LESSON, PAYMENT, USER
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case STUDENT:
                return new StudentBOImpl();
//            case COURSE:
//                return new CourseBOImpl();
//            case INSTRUCTOR:
//                return new InstructorBOImpl();
//            case LESSON:
//                return new LessonBOImpl();
//            case PAYMENT:
//                return new PaymentBOImpl();
            case USER:
                return new UserBOImpl();
            default:
                return null;
        }
    }
}