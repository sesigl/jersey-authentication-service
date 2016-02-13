package tokenBasedAuthentification;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import tokenBasedAuthentification.provider.AuthSecurityInterceptor;
import tokenBasedAuthentification.provider.TransactionalBeginIntercepter;
import tokenBasedAuthentification.provider.TransactionalEndIntercepter;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {

    private Main() {}

    // Base URI the Grizzly HTTP server will listen on
    private static final String BASE_URI = "http://localhost:%d/myapp/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(int port) {
        // create a resource config that scans for JAX-RS resources and providers
        final ResourceConfig rc = new ResourceConfig().packages("tokenBasedAuthentification.rest");
        rc.register(AuthSecurityInterceptor.class);
        rc.register(TransactionalBeginIntercepter.class);
        rc.register(TransactionalEndIntercepter.class);


        return GrizzlyHttpServerFactory.createHttpServer(URI.create(getBaseUriString(port)), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer(8080);
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));

        waitForUserInput();

        server.shutdown();
        System.out.println("Server stopped");
    }

    private static void waitForUserInput() throws IOException {
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
    }

    public static String getBaseUriString(int port) {
        return String.format(BASE_URI, port);
    }
}

