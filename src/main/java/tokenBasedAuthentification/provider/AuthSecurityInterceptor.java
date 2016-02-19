package tokenBasedAuthentification.provider;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthSecurityInterceptor implements ContainerRequestFilter {

    // 401 - Access denied
    private static final Response ACCESS_UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).build();

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
//        // Get AuthId and AuthToken from HTTP-Header.
//        String authId = requestContext.getHeaderString(AuthAccessElement.PARAM_AUTH_ID);
//        String authToken = requestContext.getHeaderString(AuthAccessElement.PARAM_AUTH_TOKEN);
//
//        //requestContext.setSecurityContext(new Authorizer(authId, authToken));
//
//        // Get method invoked.
//        Method methodInvoked = resourceInfo.getResourceMethod();
//
//        if (methodInvoked.isAnnotationPresent(RolesAllowed.class)) {
//            RolesAllowed rolesAllowedAnnotation = methodInvoked.getAnnotation(RolesAllowed.class);
//            Set<String> rolesAllowed = new HashSet<>(Arrays.asList(rolesAllowedAnnotation.value()));
//
//            Auth auth = new Auth(new UserDao());
//            if (!auth.isAuthorized(authId, authToken)) {
//                requestContext.abortWith(ACCESS_UNAUTHORIZED);
//            }
//
//        }
    }
}