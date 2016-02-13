package tokenBasedAuthentification.security;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import tokenBasedAuthentification.hibernate.HibernateFactory;

import javax.crypto.SecretKeyFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@RunWith(JMockit.class)
public class HashUtilsTest {

    @Test
    public void randomSaltIsRandom() throws Exception {
        Set<byte[]> salts = new HashSet<>();

        for(int i = 0; i < 10000;i++) {
            byte[] salt = HashUtils.GetNextSalt();
            if(!salts.contains(salt)) {
                salts.add(salt);
            } else {
                Assert.fail("Duplicated salt");
            }
        }

    }

    @Test
    public void HashingResultsInAPredictableHash() throws Exception {
        Random random = new Random();

        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        String randomString = new String(randomBytes);
        byte[] bytes = HashUtils.GetNextSalt();

        byte[] hash = HashUtils.Hash(randomString.toCharArray(), bytes);
        for(int i = 0; i < 20; i++) {
            Assert.assertTrue(Arrays.equals(hash, HashUtils.Hash(randomString.toCharArray(), bytes)));
        }
    }



    @Test(expected = RuntimeException.class)
    public void HashingThrowsAnExceptionOnError( @Mocked SecretKeyFactory unused) throws Exception {
        new Expectations() {
            {
                SecretKeyFactory.getInstance(withSubstring("PBKDF2WithHmacSHA1"));result= new NoSuchAlgorithmException("injected exception");
            }};
        HashUtils.Hash("password".toCharArray(), "SALT".getBytes());
    }

    @Test
    public void constructorIsPrivate() throws Exception {
        Constructor<HashUtils> constructor = HashUtils.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
