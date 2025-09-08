package lk.ijse.elitedrivingschoolsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Course {

    @Id
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "fee", nullable = false)
    private double fee;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY) // Added LAZY fetch
    private List<Student> students = new ArrayList<>(); // Initialize to avoid NullPointerException

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    @ManyToMany(mappedBy = "courses", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY) // Added LAZY fetch
    private List<Instructor> instructors = new ArrayList<>(); // Initialize to avoid NullPointerException
}