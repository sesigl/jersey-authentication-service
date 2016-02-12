package tokenBasedAuthentification.provider;

import tokenBasedAuthentification.dao.UserDao;
import tokenBasedAuthentification.dao.exception.UserNotFoundExcpetion;
import tokenBasedAuthentification.vo.AuthAccessElement;
import tokenBasedAuthentification.useCase.auth.Auth;

import javax.annotation.Priority;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthSecurityInterceptor implements ContainerRequestFilter {

    // 401 - Access denied
    private static final Response ACCESS_UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).build();

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get AuthId and AuthToken from HTTP-Header.
        String authId = requestContext.getHeaderString(AuthAccessElement.PARAM_AUTH_ID);
        String authToken = requestContext.getHeaderString(AuthAccessElement.PARAM_AUTH_TOKEN);

        //requestContext.setSecurityContext(new Authorizer(authId, authToken));

        // Get method invoked.
        Method methodInvoked = resourceInfo.getResourceMethod();

        if (methodInvoked.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowedAnnotation = methodInvoked.getAnnotation(RolesAllowed.class);
            Set<String> rolesAllowed = new HashSet<>(Arrays.asList(rolesAllowedAnnotation.value()));

            Auth auth = new Auth(new UserDao());
            try {
                if (!auth.isAuthorized(authId, authToken)) {
                    requestContext.abortWith(ACCESS_UNAUTHORIZED);
                }
            } catch ( UserNotFoundExcpetion e) {
                requestContext.abortWith(ACCESS_UNAUTHORIZED);
            }

        }
    }
}