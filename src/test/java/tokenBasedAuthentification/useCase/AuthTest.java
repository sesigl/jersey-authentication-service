package tokenBasedAuthentification.useCase;


import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tokenBasedAuthentification.dao.UserDao;
import tokenBasedAuthentification.dao.exception.UserNotFoundException;
import tokenBasedAuthentification.dto.*;
import tokenBasedAuthentification.useCase.auth.Auth;
import tokenBasedAuthentification.useCase.auth.exception.NotUniqueException;

@RunWith(HierarchicalContextRunner.class)
public class AuthTest {

    private Auth auth;
    private RegisterResultElement register;

    @Before
    public void initAuthMethod() {
        auth = new Auth(new UserDao());
        register = auth.register(new AuthRegisterElement("name", "password", "email"));
    }


    @After
    public void removeRegisteredUser() {
        auth.deleteUser(new AuthLoginElement("email", "password"));
    }

    public class Login {

        @Test(expected = UserNotFoundException.class)
        public void loginThrowsException_emailWrong() throws Exception {
            auth.login(new AuthLoginElement("emailWrong", "password"));
        }

        @Test(expected = UserNotFoundException.class)
        public void loginThrowsException_passwordWrong() throws Exception {
            auth.login(new AuthLoginElement("email", "passwordWrong"));
        }

        @Test(expected = UserNotFoundException.class)
        public void loginThrowsException_userNotActivated() throws Exception {
            auth.login(new AuthLoginElement("email", "password"));
        }

        @Test
        public void loginContainsInformation() throws Exception {
            AuthAccessElement login = activateAndLogin("email", "password");
            Assert.assertEquals(login.getAuthId(), "email");
            Assert.assertEquals(login.getAuthPermission(), "user");
            Assert.assertTrue(login.getAuthToken().length() > 0);
        }

        public class QuickAuthorize {

            @Test
            public void canQuickAuthUsingAuthToken() {
                AuthAccessElement login = activateAndLogin("email", "password");
                Assert.assertTrue(auth.isAuthorized("email", login.getAuthToken()));
            }

            @Test(expected = UserNotFoundException.class)
            public void wrongAuthTokenThrowsException() {
                auth.login(new AuthLoginElement("email", "password"));
                Assert.assertTrue(auth.isAuthorized("email", "justWrongToken"));
            }

            @Test(expected = UserNotFoundException.class)
            public void wrongEmailThrowsException() {
                AuthAccessElement login = auth.login(new AuthLoginElement("email", "password"));
                auth.isAuthorized("emailWrong", login.getAuthToken());
            }
        }

    }

    public class Register {
        @Test(expected = NotUniqueException.class)
        public void registerTwoTimesThrowsAnException() throws Exception {
            auth.register(new AuthRegisterElement("name", "password", "email"));
        }

        @Test
        public void userCanRegisterOnce() throws Exception {
            auth.activate(new AuthActivateElement(register.email, register.activationKey));
            auth.login(new AuthLoginElement("email", "password"));
        }

        @Test
        public void registerContainsInformation() throws Exception {
            Assert.assertEquals(register.email, "email");
            Assert.assertEquals(register.message, "Activation link sent");
        }
    }

    public class Activate {
        @Test(expected = UserNotFoundException.class)
        public void activationForNonExistingUserDoesNotWork() {
            auth.activate(new AuthActivateElement("emailWrong", register.activationKey));
        }

        @Test
        public void activateWorksForValidEmailAndKey() {
            ActivateResultElement activation = auth.activate(new AuthActivateElement("email", register.activationKey));
            Assert.assertEquals(activation.email, "email");
            Assert.assertEquals(activation.message, "activation successful");
        }
    }

    public class Delete {
        @Test(expected = UserNotFoundException.class)
        public void deleteUserForUnknownUserThrowsExcpetion() {
            auth.deleteUser(new AuthLoginElement("emailWrong", "..."));
        }

        @Test(expected = UserNotFoundException.class)
        public void deleteUserForWrongPasswordThrowsExcpetion() {
            auth.deleteUser(new AuthLoginElement(register.email, "..."));
        }
    }

    private AuthAccessElement activateAndLogin(String email, String password) {
        auth.activate(new AuthActivateElement(register.email, register.activationKey));
        return auth.login(new AuthLoginElement(email, password));
    }

}