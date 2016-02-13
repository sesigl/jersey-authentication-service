package tokenBasedAuthentification;

import org.junit.Assert;
import org.junit.Test;
import tokenBasedAuthentification.security.HashUtils;
import tokenBasedAuthentification.vo.AuthRegisterElement;
import tokenBasedAuthentification.vo.RegisterResultElement;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class MainTest {
    @Test
    public void mainStartsTheServer() throws Exception {
        InvokeInputStreamAfterSeconds(4);
        startServerThread();
        assertServerIsWorking();Thread.sleep(1000);
    }

    private void assertServerIsWorking() {
        Client c = ClientBuilder.newClient();
        WebTarget target = c.target(Main.getBaseUriString(8080));
        RegisterResultElement registerResultElement = target.path("myresource/register").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthRegisterElement("name", "password", "email@something.de")),
                        RegisterResultElement.class);

        assertEquals("email@something.de", registerResultElement.email);
    }

    private void startServerThread() {
        Thread t1 = new Thread(new Runnable() {
            public void run()
            {
                try {
                    Main.main(null);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});

        t1.start();
    }

    private void InvokeInputStreamAfterSeconds(final int seconds) {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                try {
                    Thread.sleep(seconds * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        };
        System.setIn(in);
    }

    @Test
    public void constructorIsPrivate() throws Exception {
        Constructor<Main> constructor = Main.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
