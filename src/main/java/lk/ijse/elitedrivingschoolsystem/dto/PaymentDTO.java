package lk.ijse.elitedrivingschoolsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class PaymentDTO {
    private  String paymentId;
    private String amount;
    private LocalDate date;
    private String StudentId;

}
