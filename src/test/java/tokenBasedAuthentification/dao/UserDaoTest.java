package tokenBasedAuthentification.dao;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tokenBasedAuthentification.dao.exception.UserNotFoundException;
import tokenBasedAuthentification.hibernate.entity.User;

@RunWith(HierarchicalContextRunner.class)
public class UserDaoTest {

    private UserDao userDao;
    private User user;

    @Before
    public void beforeEach() {
        userDao = new UserDao();
        user = new User("name", "email", "authToken", "role", "password");
        userDao.save(user);
    }

    public class Create {
        @After
        public void afterEach() {
            if(user != null) {
                userDao.delete(user);
            }
        }

        @Test
        public void shouldGenerateAnIdForNewUsers() throws Exception {
            Assert.assertTrue(user.id > 0 );
        }

        @Test
        public void shouldSaveAllFieldsIntoTheDatabase() throws Exception {
            User byUsernameAndPassword = userDao.findByEmailAndPassword("email", "password");
            Assert.assertTrue(user.equals(byUsernameAndPassword));
        }
    }

    public class Update {

        private int id;

        @Before
        public void beforeEach() {
            id = user.id;
            user.password = "myNamePassword";
            userDao.save(user);
        }

        @After
        public void afterEach() {
            if(user != null) {
                userDao.delete(user);
            }
        }

        @Test
        public void passwordIsChangeable() throws Exception {
            User byUsernameAndPassword = userDao.findByEmailAndPassword("email", "myNamePassword");
            Assert.assertEquals(id, byUsernameAndPassword.id);
        }

        @Test(expected = UserNotFoundException.class)
        public void passwordIsChangeable_oldPasswordShouldNotWorkAnymore() throws Exception {
            User byUsernameAndPassword = userDao.findByEmailAndPassword("email", "password");
            Assert.assertEquals(id, byUsernameAndPassword.id);
        }
    }

    public class Read {
        @After
        public void afterEach() {
            if(user != null) {
                userDao.delete(user);
            }
        }

        @Test
        public void shouldSelectTheUserByCorrectNameAndPassword() throws Exception {
            User byUsernameAndPassword = userDao.findByEmailAndPassword("email", "password");
            Assert.assertTrue(user.equals(byUsernameAndPassword));
        }

        @Test(expected = UserNotFoundException.class)
        public void shouldNotSelectTheUserByWrongEmail() throws Exception {
            User byUsernameAndPassword = userDao.findByEmailAndPassword("nameNotExisting", "password");
            Assert.assertTrue(user.equals(byUsernameAndPassword));
        }

        @Test(expected = UserNotFoundException.class)
        public void shouldNotSelectTheUserByWrongPassword() throws Exception {
            User byUsernameAndPassword = userDao.findByEmailAndPassword("email", "wrongPassword");
            Assert.assertTrue(user.equals(byUsernameAndPassword));
        }

        @Test
        public void shouldGetUserByEmailAndAuthToken() {
            User byUsernameAndPassword = userDao.findByEmailAndAuthToken("email", "authToken");
            Assert.assertTrue(user.equals(byUsernameAndPassword));
        }

        @Test(expected = UserNotFoundException.class)
        public void shouldGetUserByEmailAndAuthToken_wrongEmail() {
            User byUsernameAndPassword = userDao.findByEmailAndAuthToken("emailWrong", "authToken");
            Assert.assertTrue(user.equals(byUsernameAndPassword));
        }

        @Test(expected = UserNotFoundException.class)
        public void shouldGetUserByEmailAndAuthToken_wrongToken() {
            User byUsernameAndPassword = userDao.findByEmailAndAuthToken("email", "authTokenWrong");
            Assert.assertTrue(user.equals(byUsernameAndPassword));
        }

        @Test
        public void shouldGetUserByEmail() {
            User byUsernameAndPassword = userDao.findByEmail("email");
            Assert.assertTrue(user.equals(byUsernameAndPassword));
        }

        @Test(expected = UserNotFoundException.class)
        public void shouldGetUserByEmail_wrongEmail() {
            User byUsernameAndPassword = userDao.findByEmail("emailWrong");
            Assert.assertTrue(user.equals(byUsernameAndPassword));
        }

        @Test
        public void checkIfThereIsAlreadyAnUserWithTheGivenEmail() {
            Assert.assertTrue(userDao.hasUserWithEmail("email"));
        }

        @Test
        public void checkIfThereIsAlreadyAnUserWithTheGivenEmail_returnsFalseOnNotFound() {
            Assert.assertFalse(userDao.hasUserWithEmail("emailWrong"));
        }

        @Test(expected = UserNotFoundException.class)
        public void findUserByEmailAndActivationKey_throwsExceptionOnInvalidEmail() {
            userDao.findByEmailAndActivationKey("wrongEmail", "validKey");
        }

        @Test(expected = UserNotFoundException.class)
        public void findUserByEmailAndActivationKey_throwsExceptionOnInvalidKey() {
            userDao.findByEmailAndActivationKey("email", "invalidKey");
        }

        @Test
        public void findUserByEmailAndActivationKey() {
            user.activationKey = "validKey";
            userDao.save(user);
            userDao.findByEmailAndActivationKey("email", "validKey");
        }
    }

    public class Delete {

        @Test(expected = UserNotFoundException.class)
        public void deleteTheGivenUser() throws Exception {
            User byUsernameAndPassword = userDao.findByEmailAndPassword("email", "password");
            userDao.delete(byUsernameAndPassword);
            userDao.findByEmailAndPassword("email", "password");
        }
    }

}
