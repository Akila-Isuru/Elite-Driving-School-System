package lk.ijse.elitedrivingschoolsystem.bo.util;

import lk.ijse.elitedrivingschoolsystem.dto.CourseDTO;
import lk.ijse.elitedrivingschoolsystem.dto.InstructorDTO;
import lk.ijse.elitedrivingschoolsystem.dto.LessonDto;
import lk.ijse.elitedrivingschoolsystem.dto.PaymentDTO;
import lk.ijse.elitedrivingschoolsystem.dto.StudentDTO;
import lk.ijse.elitedrivingschoolsystem.dto.UserDTO;
import lk.ijse.elitedrivingschoolsystem.entity.Course;
import lk.ijse.elitedrivingschoolsystem.entity.Instructor;
import lk.ijse.elitedrivingschoolsystem.entity.Lesson;
import lk.ijse.elitedrivingschoolsystem.entity.Payment;
import lk.ijse.elitedrivingschoolsystem.entity.Student;
import lk.ijse.elitedrivingschoolsystem.entity.User;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class EntityDTOConverter {

    public Student getStudent(StudentDTO dto) {
        Student student = new Student();
        student.setStudentId(dto.getStudentId());
        student.setName(dto.getName());
        student.setContactNumber(dto.getContactNumber());
        student.setEmail(dto.getEmail());
        student.setRegDate(dto.getRegDate());
        return student;
    }

    public StudentDTO getStudentDTO(Student entity) {
        StudentDTO dto = new StudentDTO();
        dto.setStudentId(entity.getStudentId());
        dto.setName(entity.getName());
        dto.setContactNumber(entity.getContactNumber());
        dto.setEmail(entity.getEmail());
        dto.setRegDate(entity.getRegDate());
        if (entity.getCourses() != null) {
            dto.setCourseIds(entity.getCourses().stream()
                    .map(Course::getCourseId)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public User getUser(UserDTO dto) {
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUsername(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }

    public UserDTO getUserDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setUserId(entity.getUserId());
        dto.setUserName(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        return dto;
    }

    public Course getCourse(CourseDTO dto) {
        Course course = new Course();
        course.setCourseId(dto.getCourseId());
        course.setCourseName(dto.getCourseName());
        course.setDuration(dto.getDuration());
        course.setFee(Double.parseDouble(dto.getFee()));
        return course;
    }

    public CourseDTO getCourseDTO(Course entity) {
        CourseDTO dto = new CourseDTO();
        dto.setCourseId(entity.getCourseId());
        dto.setCourseName(entity.getCourseName());
        dto.setDuration(entity.getDuration());
        dto.setFee(String.valueOf(entity.getFee()));
        return dto;
    }

    public Instructor getInstructor(InstructorDTO dto) {
        Instructor instructor = new Instructor();
        instructor.setInstructorId(dto.getInstructorId());
        instructor.setName(dto.getName());
        instructor.setContactNumber(dto.getContactNumber());
        instructor.setEmail(dto.getEmail());
        return instructor;
    }

    public InstructorDTO getInstructorDTO(Instructor entity) {
        InstructorDTO dto = new InstructorDTO();
        dto.setInstructorId(entity.getInstructorId());
        dto.setName(entity.getName());
        dto.setContactNumber(entity.getContactNumber());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    public Lesson getLesson(LessonDto dto) {
        Lesson lesson = new Lesson();
        lesson.setLessonId(dto.getLessonId());
        lesson.setDate(dto.getDate());
        lesson.setTime(dto.getTime());
        return lesson;
    }

    public LessonDto getLessonDTO(Lesson entity) {
        LessonDto dto = new LessonDto();
        dto.setLessonId(entity.getLessonId());
        dto.setDate(entity.getDate());
        dto.setTime(entity.getTime());
        dto.setStudentId(entity.getStudent().getStudentId());
        dto.setInstructorId(entity.getInstructor().getInstructorId());
        dto.setCourseId(entity.getCourse().getCourseId());
        return dto;
    }

    public Payment getPayment(PaymentDTO dto) {
        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setAmount(Double.parseDouble(dto.getAmount()));
        payment.setDate(LocalDate.now());
        return payment;
    }

    public PaymentDTO getPaymentDTO(Payment entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(entity.getPaymentId());
        dto.setAmount(String.valueOf(entity.getAmount()));
        dto.setDate(entity.getDate());
        dto.setStudentId(entity.getStudent().getStudentId());
        return dto;
    }
}