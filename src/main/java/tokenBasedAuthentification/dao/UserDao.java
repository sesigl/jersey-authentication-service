package tokenBasedAuthentification.dao;

import com.google.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import tokenBasedAuthentification.dao.exception.UserNotFoundExcpetion;
import tokenBasedAuthentification.dao.interfaces.IUserDao;
import tokenBasedAuthentification.hibernate.HibernateFactory;
import tokenBasedAuthentification.hibernate.entity.User;

import java.util.HashMap;
import java.util.List;

public class UserDao implements IUserDao{

    @Inject
    public UserDao() {
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        HashMap<Object, Object> searchCriterias = new HashMap<Object, Object>();


        Session session = HibernateFactory.CreateNewSession();
        Criteria criteria = session.createCriteria(User.class);

        List<User> list = criteria.add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password))
                .list();

        HibernateFactory.CloseSession(session);

        if (list.size() > 0) {

            User user = list.get(0);
            return user;
        }

        throw new UserNotFoundExcpetion("No user found for given email: " + email + " and password ***");
    }

    @Override
    public User findByEmailAndAuthToken(String email, String authToken) {
        Session session = HibernateFactory.CreateNewSession();
        Criteria criteria = session.createCriteria(User.class);

        List<User> list = criteria
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("authToken", authToken))
                .list();

        HibernateFactory.CloseSession(session);

        if (list.size() > 0) {

            User user = list.get(0);
            return user;
        }

        throw new UserNotFoundExcpetion("No user found for given email: " + email + " and authToken: " + authToken);
    }

    @Override
    public User findByEmail(String email) {
        Session session = HibernateFactory.CreateNewSession();
        Criteria criteria = session.createCriteria(User.class);

        List<User> list = criteria.add(Restrictions.eq("email", email))
                .list();

        HibernateFactory.CloseSession(session);

        if (list.size() > 0) {

            User user = list.get(0);
            return user;
        }

        throw new UserNotFoundExcpetion("No user found for given email: " + email);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        Session session = HibernateFactory.CreateNewSession();
        Criteria criteria = session.createCriteria(User.class);
        List<User> list = criteria.add(Restrictions.eq("email", email))
                .list();

        HibernateFactory.CloseSession(session);

        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void save(User user) {
        Session session = HibernateFactory.CreateNewSession();
        session.saveOrUpdate(user);
        HibernateFactory.CloseSession(session);
    }

    @Override
    public void delete(User user) {
        Session session = HibernateFactory.CreateNewSession();
        session.delete(user);
        HibernateFactory.CloseSession(session);
    }

    @Override
    public User findByEmailAndActivationKey(String email, String key) {
        Session session = HibernateFactory.CreateNewSession();
        Criteria criteria = session.createCriteria(User.class);
        List<User> list = criteria
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("activationKey", key))
                .list();

        HibernateFactory.CloseSession(session);

        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new UserNotFoundExcpetion("No user found for given email: " + email + " and key ***");
        }
    }
}