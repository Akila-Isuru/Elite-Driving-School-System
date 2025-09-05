package lk.ijse.elitedrivingschoolsystem.dao.custom.impl;

import lk.ijse.elitedrivingschoolsystem.dao.custom.PaymentDAO;
import lk.ijse.elitedrivingschoolsystem.entity.Payment;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public boolean save(Payment entity, Session session) {
        try {
            session.persist(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Payment entity, Session session) {
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
            Payment payment = session.get(Payment.class, id);
            if (payment != null) {
                session.remove(payment);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Payment get(String id, Session session) {
        return session.get(Payment.class, id);
    }

    @Override
    public List<Payment> getAll(Session session) {
        Query<Payment> query = session.createQuery("FROM Payment", Payment.class);
        return query.list();
    }
}
