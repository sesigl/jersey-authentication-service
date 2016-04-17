package tokenBasedAuthentification.dao;

import com.google.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import tokenBasedAuthentification.dao.exception.UserNotFoundExcpetion;
import tokenBasedAuthentification.dao.interfaces.IUserDao;
import tokenBasedAuthentification.hibernate.HibernateUtils;
import tokenBasedAuthentification.hibernate.entity.User;

import java.util.HashMap;
import java.util.List;

public class UserDao implements IUserDao{

    @Inject
    public UserDao() {
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        HashMap<Object, Object> searchCriterias = new HashMap<>();


        Session session = HibernateUtils.createNewSession();
        Criteria criteria = session.createCriteria(User.class);

        @SuppressWarnings("unchecked")
        List<User> list = criteria.add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password))
                .list();

        HibernateUtils.closeSession(session);

        if (list.size() > 0) {
            return list.get(0);
        }

        throw new UserNotFoundExcpetion("No user found for given email: " + email + " and password ***");
    }

    @Override
    public User findByEmailAndAuthToken(String email, String authToken) {
        Session session = HibernateUtils.createNewSession();
        Criteria criteria = session.createCriteria(User.class);

        @SuppressWarnings("unchecked")
        List<User> list = criteria
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("authToken", authToken))
                .list();

        HibernateUtils.closeSession(session);

        if (list.size() > 0) {

            return list.get(0);
        }

        throw new UserNotFoundExcpetion("No user found for given email: " + email + " and authToken: " + authToken);
    }

    @Override
    public User findByEmail(String email) {
        Session session = HibernateUtils.createNewSession();
        Criteria criteria = session.createCriteria(User.class);

        @SuppressWarnings("unchecked")
        List<User> list = criteria.add(Restrictions.eq("email", email))
                .list();

        HibernateUtils.closeSession(session);

        if (list.size() > 0) {

            return list.get(0);
        }

        throw new UserNotFoundExcpetion("No user found for given email: " + email);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        Session session = HibernateUtils.createNewSession();
        Criteria criteria = session.createCriteria(User.class);

        @SuppressWarnings("unchecked")
        List<User> list = criteria.add(Restrictions.eq("email", email))
                .list();

        HibernateUtils.closeSession(session);

        return list.size() > 0;
    }

    @Override
    public void save(User user) {
        Session session = HibernateUtils.createNewSession();
        session.saveOrUpdate(user);
        HibernateUtils.closeSession(session);
    }

    @Override
    public void delete(User user) {
        Session session = HibernateUtils.createNewSession();
        session.delete(user);
        HibernateUtils.closeSession(session);
    }

    @Override
    public User findByEmailAndActivationKey(String email, String key) {
        Session session = HibernateUtils.createNewSession();
        Criteria criteria = session.createCriteria(User.class);

        @SuppressWarnings("unchecked")
        List<User> list = criteria
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("activationKey", key))
                .list();

        HibernateUtils.closeSession(session);

        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new UserNotFoundExcpetion("No user found for given email: " + email + " and key ***");
        }
    }
}