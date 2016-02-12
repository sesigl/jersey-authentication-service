package tokenBasedAuthentification.useCase.auth;

import tokenBasedAuthentification.dao.UserDao;
import tokenBasedAuthentification.dao.exception.UserNotFoundExcpetion;
import tokenBasedAuthentification.vo.*;
import tokenBasedAuthentification.hibernate.entity.User;
import tokenBasedAuthentification.security.HashUtils;
import tokenBasedAuthentification.useCase.auth.exception.NotUniqueException;

import java.util.UUID;

/**
 * # Use cases:
 * - User can sign up
 * - User can activate
 * - User can login
 * - User can stay logged in via auth token
 */
public class Auth {

    UserDao userDao;

    public Auth(UserDao userDao) {
        this.userDao = userDao;
    }

    public AuthAccessElement login(AuthLoginElement loginElement) {

        User user = userDao.findByEmail(loginElement.getEmail());

        if (new String(HashUtils.Hash(loginElement.getPassword().toCharArray(), user.salt)).equals(user.password) && user.activated) {
            user.authToken = UUID.randomUUID().toString();
            userDao.save(user);
            return new AuthAccessElement(loginElement.getEmail(), user.authToken, user.role);
        } else {
            throw new UserNotFoundExcpetion("No user found for given email: " + user.email + " and password ***");
        }
    }

    public boolean isAuthorized(String email, String authToken) {
        User user = userDao.findByEmailAndAuthToken(email, authToken);
        return user != null; //&& rolesAllowed.contains(user.getRole());
    }

    /**
     * TODO: throw AlreadUsedEmailAdressExpception on already used mail adress
     * TODO: sent email
     * TODO: remove activationKeyFromJsonWhenSendingItToClient
     *
     * @param registerElement
     * @return
     */
    public RegisterResultElement register(AuthRegisterElement registerElement) {

        if (userDao.hasUserWithEmail(registerElement.email)) {
            throw new NotUniqueException("email adress already in use");
        }

        User user = new User();

        byte[] salt = HashUtils.GetNextSalt();
        String activationKey = UUID.randomUUID().toString();

        user.password = new String(HashUtils.Hash(registerElement.password.toCharArray(), salt));
        user.email = registerElement.email;
        user.name = registerElement.name;
        user.role = "user";
        user.salt = salt;
        user.activationKey = activationKey;

        userDao.save(user);
        return new RegisterResultElement("Activation link sent", user.email, activationKey);


    }

    /**
     * TODO: implement
     *
     * @param authActivateElement
     * @return
     */
    public ActivateResultElement activate(AuthActivateElement authActivateElement) {
        User user = userDao.findByEmailAndActivationKey(authActivateElement.email, authActivateElement.activationKey);
        user.activated = true;
        userDao.save(user);
        return new ActivateResultElement("activation successful", user.email);
    }

    public void deleteUser(AuthLoginElement loginElement) {
        User user = userDao.findByEmail(loginElement.getEmail());

        if (new String(HashUtils.Hash(loginElement.getPassword().toCharArray(), user.salt)).equals(user.password)) {
            userDao.delete(user);
        } else {
            throw new UserNotFoundExcpetion("No user found for given email: " + user.email + " and password ***");
        }
    }
}