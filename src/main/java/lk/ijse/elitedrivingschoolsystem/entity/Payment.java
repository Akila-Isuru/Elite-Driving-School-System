package lk.ijse.elitedrivingschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment {

    @Id
    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
