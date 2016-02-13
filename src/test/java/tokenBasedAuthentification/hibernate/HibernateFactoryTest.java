package tokenBasedAuthentification.hibernate;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class HibernateFactoryTest {
    @Test
    public void constructorIsPrivate() throws Exception {
        Constructor<HibernateFactory> constructor = HibernateFactory.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
