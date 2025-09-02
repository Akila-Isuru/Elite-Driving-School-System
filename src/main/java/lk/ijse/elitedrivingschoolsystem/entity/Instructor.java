package lk.ijse.elitedrivingschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "instructor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Instructor {

    @Id
    @Column(name = "instructor_id")
    private String instructorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;
}