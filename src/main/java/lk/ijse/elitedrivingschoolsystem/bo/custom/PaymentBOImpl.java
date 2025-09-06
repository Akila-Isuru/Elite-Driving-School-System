package lk.ijse.elitedrivingschoolsystem.bo.custom;

import lk.ijse.elitedrivingschoolsystem.bo.custom.PaymentBO;
import lk.ijse.elitedrivingschoolsystem.bo.exception.PaymentException;
import lk.ijse.elitedrivingschoolsystem.bo.util.EntityDTOConverter;
import lk.ijse.elitedrivingschoolsystem.config.FactoryConfiguration;
import lk.ijse.elitedrivingschoolsystem.dao.DAOFactory;
import lk.ijse.elitedrivingschoolsystem.dao.DAOTypes;
import lk.ijse.elitedrivingschoolsystem.dao.custom.PaymentDAO;
import lk.ijse.elitedrivingschoolsystem.dao.custom.StudentDAO;
import lk.ijse.elitedrivingschoolsystem.dto.PaymentDTO;
import lk.ijse.elitedrivingschoolsystem.entity.Payment;
import lk.ijse.elitedrivingschoolsystem.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentBOImpl implements PaymentBO {
    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOTypes.PAYMENT);
    private final StudentDAO studentDAO = DAOFactory.getInstance().getDAO(DAOTypes.STUDENT);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public boolean processPayment(PaymentDTO dto) throws PaymentException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Student student = studentDAO.get(dto.getStudentId(), session);
            if (student == null) {
                throw new PaymentException("Student not found for payment.");
            }
            Payment payment = converter.getPayment(dto);
            payment.setStudent(student);
            payment.setDate(LocalDate.now());

            boolean isSaved = paymentDAO.save(payment, session);
            if (isSaved) {
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new PaymentException("Payment processing failed: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            List<Payment> payments = paymentDAO.getAll(session);
            return payments.stream()
                    .map(converter::getPaymentDTO)
                    .collect(Collectors.toList());
        } finally {
            session.close();
        }
    }
}
