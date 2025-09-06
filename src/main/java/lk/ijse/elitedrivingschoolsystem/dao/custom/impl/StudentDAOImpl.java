package lk.ijse.elitedrivingschoolsystem.dao.custom.impl;

import lk.ijse.elitedrivingschoolsystem.dao.custom.StudentDAO;
import lk.ijse.elitedrivingschoolsystem.entity.Student;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public boolean save(Student entity, Session session) {
        try {
            session.persist(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Student entity, Session session) {
        try {
            session.merge(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id, Session session) {
        try {
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.remove(student);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Student get(String id, Session session) {
        return session.get(Student.class, id);
    }

    @Override
    public List<Student> getAll(Session session) {
        Query<Student> query = session.createQuery("FROM Student", Student.class);
        return query.list();
    }

    @Override
    public List<Student> getStudentsInAllCourses(Session session) {
        String hql = "SELECT s FROM Student s " +
                "WHERE SIZE(s.courses) = (SELECT COUNT(c.courseId) FROM Course c)";
        Query<Student> query = session.createQuery(hql, Student.class);
        return query.list();
    }
}
