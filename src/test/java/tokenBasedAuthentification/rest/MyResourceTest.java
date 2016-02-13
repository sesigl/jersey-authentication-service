package tokenBasedAuthentification.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.*;
import tokenBasedAuthentification.Main;
import tokenBasedAuthentification.vo.*;

import static org.junit.Assert.assertEquals;

public class MyResourceTest {

    private static HttpServer server;
    private static WebTarget target;

    @BeforeClass
    public static void setUp() throws Exception {
        server = Main.startServer(8081);
        Client c = ClientBuilder.newClient();
        target = c.target(Main.getBaseUriString(8081));
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
        assertAuthRequiredMethodAccess(authAccessElement);
    }

    @Test
    public void userNoAccess() {
        Response result = target.path("myresource/test").request(MediaType.APPLICATION_JSON_TYPE)
                .header(AuthAccessElement.PARAM_AUTH_ID, "AA")
                .header(AuthAccessElement.PARAM_AUTH_TOKEN, "BB")
                .get(Response.class);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), result.getStatus());
    }

    private void assertAuthRequiredMethodAccess(AuthAccessElement authAccessElement) {
        TestDataContainer result = target.path("myresource/test").request(MediaType.APPLICATION_JSON_TYPE)
                .header(AuthAccessElement.PARAM_AUTH_ID, authAccessElement.getAuthId())
                .header(AuthAccessElement.PARAM_AUTH_TOKEN, authAccessElement.getAuthToken())
                .get(TestDataContainer.class);
        assertEquals("A", result.a);
    }

    private AuthAccessElement assertLogin(String email) {
        AuthAccessElement authAccessElement = target.path("myresource/login").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthLoginElement(email, "password")),
                        AuthAccessElement.class);
        assertEquals(email, authAccessElement.getAuthId());
        return authAccessElement;
    }

    private void assertActivate(String email, RegisterResultElement registerResultElement) {
        ActivateResultElement activateResultElement = target.path("myresource/activate").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthActivateElement(email, registerResultElement.activationKey)),
                        ActivateResultElement.class);
        assertEquals("activation successful", activateResultElement.message);

    }

    private RegisterResultElement assertRegister(String email) {
        RegisterResultElement registerResultElement = target.path("myresource/register").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthRegisterElement("name", "password", email)),
                        RegisterResultElement.class);
        assertEquals(email, registerResultElement.email);
        return registerResultElement;
    }
}
