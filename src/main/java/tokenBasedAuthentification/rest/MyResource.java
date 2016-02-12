package tokenBasedAuthentification.rest;

import tokenBasedAuthentification.dao.UserDao;
import tokenBasedAuthentification.vo.*;
import tokenBasedAuthentification.useCase.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("myresource")
public class MyResource {

    @POST
	@Path("login")
	public AuthAccessElement login(AuthLoginElement loginElement) {
        Auth auth = new Auth(new UserDao());
		return auth.login(loginElement);
	}

    @POST
	@Path("register")
	public RegisterResultElement register(AuthRegisterElement registerElement) {
        //		validate(registerElement.email); //TODO: move into business logic layer
        Auth auth = new Auth(new UserDao());
        return auth.register(registerElement);

	}

    //TEST, TODO: check if what happens if there is no empty constructor, in prod it will fail for pojos
	@POST
	@Path("activate")
	public ActivateResultElement activate(AuthActivateElement authActivateElement) {
        Auth auth = new Auth(new UserDao());
        return auth.activate(authActivateElement);
	}

	@GET
	@Path("test")
	@RolesAllowed("user")
	public TestDataContainer test() {
		return new TestDataContainer("A", "B", "C");

	}
}
