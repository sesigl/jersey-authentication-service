package tokenBasedAuthentification.useCase.auth;

import com.google.inject.Inject;
import tokenBasedAuthentification.dao.exception.UserNotFoundExcpetion;
import tokenBasedAuthentification.dao.interfaces.IUserDao;
import tokenBasedAuthentification.hibernate.entity.User;
import tokenBasedAuthentification.security.HashUtils;
import tokenBasedAuthentification.useCase.auth.exception.NotUniqueException;
import tokenBasedAuthentification.useCase.auth.interfaces.IAuth;
import tokenBasedAuthentification.dto.*;

import java.util.UUID;

/**
 * # Use cases:
 * - User can sign up
 * - User can activate
 * - User can login
 * - User can stay logged in via auth token
 */
public class Auth implements IAuth {

    private IUserDao userDao;

    @Inject
    public Auth(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
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

    @Override
    public boolean isAuthorized(String email, String authToken) {
        try {
            //&& rolesAllowed.contains(user.getRole());
            return userDao.findByEmailAndAuthToken(email, authToken) != null;
        } catch (UserNotFoundExcpetion e ) {
            return false;
        }
    }

    /**
     * TODO: send email
     * TODO: remove activationKeyFromJsonWhenSendingItToClient
     * @param registerElement Contains user credentials
     * @return Registration result
     */
    @Override
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
     * @param authActivateElement Auth related user credentials
     * @return Activate result data
     */
    @Override
    public ActivateResultElement activate(AuthActivateElement authActivateElement) {
        User user = userDao.findByEmailAndActivationKey(authActivateElement.email, authActivateElement.activationKey);
        user.activated = true;
        userDao.save(user);
        return new ActivateResultElement("activation successful", user.email);
    }

    @Override
    public void deleteUser(AuthLoginElement loginElement) {
        User user = userDao.findByEmail(loginElement.getEmail());

        if (new String(HashUtils.Hash(loginElement.getPassword().toCharArray(), user.salt)).equals(user.password)) {
            userDao.delete(user);
        } else {
            throw new UserNotFoundExcpetion("No user found for given email: " + user.email + " and password ***");
        }
    }
}