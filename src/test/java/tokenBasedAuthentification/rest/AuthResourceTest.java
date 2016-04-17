package tokenBasedAuthentification.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tokenBasedAuthentification.Main;
import tokenBasedAuthentification.dto.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class AuthResourceTest {

    private static HttpServer server;
    private static WebTarget target;

    @BeforeClass
    public static void setUp() throws Exception {
        server = Main.startServer(8080);
        Client c = ClientBuilder.newClient();
        target = c.target(Main.getBaseUriString(8080));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void userLoginUseCase() {
        String email = "email2@something.de";

        RegisterResultElement registerResultElement = assertRegister(email);
        assertActivate(email, registerResultElement);

        AuthAccessElement authAccessElement = assertLogin(email);
        assertCheckAccessIsTrue(authAccessElement);
    }

    @Test
    public void userNoAccess() {
        CheckAuthElement result = target.path("auth/check").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthAccessElement("wrong", "wrong", "user")), CheckAuthElement.class);
        assertEquals(false, result.authorized);
    }

    private void assertCheckAccessIsTrue(AuthAccessElement authAccessElement) {
        CheckAuthElement result = target.path("auth/check").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthAccessElement(authAccessElement.getAuthId(), authAccessElement.getAuthToken(), "user")), CheckAuthElement.class);
        assertEquals(true, result.authorized);
    }

    private AuthAccessElement assertLogin(String email) {
        AuthAccessElement authAccessElement = target.path("auth/login").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthLoginElement(email, "password")),
                        AuthAccessElement.class);
        assertEquals(email, authAccessElement.getAuthId());
        return authAccessElement;
    }

    private void assertActivate(String email, RegisterResultElement registerResultElement) {
        ActivateResultElement activateResultElement = target.path("auth/activate").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthActivateElement(email, registerResultElement.activationKey)),
                        ActivateResultElement.class);
        assertEquals("activation successful", activateResultElement.message);

    }

    private RegisterResultElement assertRegister(String email) {
        RegisterResultElement registerResultElement = target.path("auth/register").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthRegisterElement("name", "password", email)),
                        RegisterResultElement.class);
        assertEquals(email, registerResultElement.email);
        return registerResultElement;
    }
}
