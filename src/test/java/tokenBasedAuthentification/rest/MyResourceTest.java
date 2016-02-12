package tokenBasedAuthentification.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tokenBasedAuthentification.Main;
import tokenBasedAuthentification.vo.*;

import static org.junit.Assert.assertEquals;

public class MyResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void userLoginUseCase() {
        RegisterResultElement registerResultElement = target.path("myresource/register").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthRegisterElement("name", "password", "email@something.de")),
                        RegisterResultElement.class);
        assertEquals("email@something.de", registerResultElement.email);

        ActivateResultElement activateResultElement = target.path("myresource/activate").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthActivateElement("email@something.de", registerResultElement.activationKey)),
                        ActivateResultElement.class);
        assertEquals("activation successful", activateResultElement.message);

        AuthAccessElement authAccessElement = target.path("myresource/login").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new AuthLoginElement("email@something.de", "password")),
                        AuthAccessElement.class);
        assertEquals(18, authAccessElement.getAuthId().length());

        TestDataContainer result = target.path("myresource/test").request(MediaType.APPLICATION_JSON_TYPE)
                .header(AuthAccessElement.PARAM_AUTH_ID, authAccessElement.getAuthId())
                .header(AuthAccessElement.PARAM_AUTH_TOKEN, authAccessElement.getAuthToken())
                .get(TestDataContainer.class);
        assertEquals("A", result.a);
    }

    @Test
    public void userNoAccess() {
        Response result = target.path("myresource/test").request(MediaType.APPLICATION_JSON_TYPE)
                .header(AuthAccessElement.PARAM_AUTH_ID, "AA")
                .header(AuthAccessElement.PARAM_AUTH_TOKEN, "BB")
                .get(Response.class);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), result.getStatus());
    }
}
