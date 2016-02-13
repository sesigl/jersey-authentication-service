package tokenBasedAuthentification.hibernate;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tokenBasedAuthentification.hibernate.entity.User;

@RunWith(HierarchicalContextRunner.class)
public class UserTest {

    private User user;


    @Before
    public void setUp() throws Exception {
        user = new User("name", "email", "token", "role", "password");
    }

    @Test
    public void equalsComparesUsersData() throws Exception {
        User user2 = new User("name", "email", "token", "role", "password");
        Assert.assertTrue(user.equals(user2));
    }

    public class relevantCompareAttributes {
        @Test
        public void equalsComparesUsersData_name() throws Exception {
            User user2 = new User("name2", "email", "token", "role", "password");
            Assert.assertFalse(user.equals(user2));
        }

        @Test
        public void equalsComparesUsersData_email() throws Exception {
            User user2 = new User("name", "email2", "token", "role", "password");
            Assert.assertFalse(user.equals(user2));
        }

        @Test
        public void equalsComparesUsersData_token() throws Exception {
            User user2 = new User("name", "email", "token2", "role", "password");
            Assert.assertFalse(user.equals(user2));
        }

        @Test
        public void equalsComparesUsersData_role() throws Exception {
            User user2 = new User("name", "email", "token", "role2", "password");
            Assert.assertFalse(user.equals(user2));
        }
    }

    public class nonRelevantCompareAttributes {
        @Test
        public void equalsComparesUsersDataExcept_id() throws Exception {
            user.id = 1;
            User user2 = new User("name", "email", "token", "role", "password");
            user2.id = 2;
            Assert.assertTrue(user.equals(user2));
        }

        @Test
        public void equalsComparesUsersDataExcept_password() throws Exception {
            User user2 = new User("name", "email", "token", "role", "password2");
            Assert.assertTrue(user.equals(user2));
        }

        @Test
        public void equalsComparesUsersDataExcept_activated() throws Exception {
            user.activated = true;
            User user2 = new User("name", "email", "token", "role", "password2");
            user2.activated = false;

            Assert.assertTrue(user.equals(user2));
        }

        @Test
        public void equalsComparesUsersDataExcept_activationKey() throws Exception {
            user.activationKey = "123";
            User user2 = new User("name", "email", "token", "role", "password2");
            user2.activationKey = "456";

            Assert.assertTrue(user.equals(user2));
        }

        @Test
        public void equalsComparesUsersDataExcept_salt() throws Exception {
            user.salt = "123".getBytes();
            User user2 = new User("name", "email", "token", "role", "password2");
            user2.salt = "456".getBytes();

            Assert.assertTrue(user.equals(user2));
        }
    }


}
