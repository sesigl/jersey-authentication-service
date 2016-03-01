package tokenBasedAuthentification.dao.interfaces;

import tokenBasedAuthentification.hibernate.entity.User;

public interface IUserDao {
    User findByEmailAndPassword(String email, String password);

    User findByEmailAndAuthToken(String email, String authToken);

    User findByEmail(String email);

    boolean hasUserWithEmail(String email);

    void save(User user);

    void delete(User user);

    User findByEmailAndActivationKey(String email, String key);
}
