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
        user.id = 1;
    }

    @Test
    public void equalsComparesUsersData() throws Exception {
        User user2 = new User("name", "email", "token", "role", "password");
        user2.id = 1;
        Assert.assertTrue(user.equals(user2));
    }

    public class relevantCompareAttributes {

        @Test
        public void equalsComparesUsersData_email() throws Exception {
            User user2 = new User("name", "email2", "token", "role", "password");
            Assert.assertFalse(user.equals(user2));
        }

        @Test
        public void equalsComparesUsersData_id() throws Exception {
            User user2 = new User("name", "email", "token", "role", "password");
            user2.id = 9;
            Assert.assertFalse(user.equals(user2));
        }
    }

    public class nonRelevantCompareAttributes {
        @Test
        public void equalsComparesUsersBusinessData() throws Exception {
            user.id = 1;
            User user2 = new User("nameNE", "email", "tokenNE", "roleNE", "passwordNE");
            user2.id = 1;
            Assert.assertTrue(user.equals(user2));
        }
    }


}
