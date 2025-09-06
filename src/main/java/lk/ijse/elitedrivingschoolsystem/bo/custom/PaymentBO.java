package lk.ijse.elitedrivingschoolsystem.bo.custom;

import lk.ijse.elitedrivingschoolsystem.bo.SuperBO;
import lk.ijse.elitedrivingschoolsystem.bo.exception.PaymentException;
import lk.ijse.elitedrivingschoolsystem.dto.PaymentDTO;

import java.util.List;

public interface PaymentBO extends SuperBO {
    boolean processPayment(PaymentDTO dto) throws PaymentException;
    List<PaymentDTO> getAllPayments();
}
