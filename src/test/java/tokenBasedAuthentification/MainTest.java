package tokenBasedAuthentification;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@RunWith(JMockit.class)
public class MainTest {
    @Test
    public void mainStartsTheServer() throws Exception {
        final boolean[] serverStarted = mockStartServer();

        invokeUserInput();

        Main.main(null);
        Assert.assertTrue(serverStarted[0]);
    }

    private boolean[] mockStartServer() {
        final boolean[] serverStarted = {false};
        new MockUp<Main>() {
            @Mock
            public HttpServer startServer(int port) {
                serverStarted[0] = true;
                return new HttpServer();
            }
        };
        return serverStarted;
    }

    private void invokeUserInput() {
        InputStream in = new ByteArrayInputStream("\n".getBytes());
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
