package tokenBasedAuthentification;

import mockit.*;
import mockit.integration.junit4.JMockit;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.utils.BufferInputStream;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import tokenBasedAuthentification.provider.AuthSecurityInterceptor;
import tokenBasedAuthentification.provider.TransactionalBeginIntercepter;
import tokenBasedAuthentification.provider.TransactionalEndIntercepter;
import tokenBasedAuthentification.security.HashUtils;
import tokenBasedAuthentification.vo.AuthRegisterElement;
import tokenBasedAuthentification.vo.RegisterResultElement;

import javax.crypto.SecretKeyFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import static mockit.internal.util.MethodReflection.invoke;
import static org.junit.Assert.assertEquals;

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
