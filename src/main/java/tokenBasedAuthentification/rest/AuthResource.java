package tokenBasedAuthentification.rest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import tokenBasedAuthentification.config.AuthModule;
import tokenBasedAuthentification.dto.*;
import tokenBasedAuthentification.useCase.auth.interfaces.IAuth;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("auth")
public class AuthResource {

    @POST
	@Path("login")
	public AuthAccessElement login(AuthLoginElement loginElement) {
		return getAuth().login(loginElement);
	}

	@POST
	@Path("register")
	public RegisterResultElement register(AuthRegisterElement registerElement) {
        //		validate(registerElement.email); //TODO: move into business logic layer
        return getAuth().register(registerElement);

	}

    //TEST, TODO: check if what happens if there is no empty constructor, in prod it will fail for pojos
	@POST
	@Path("activate")
	public ActivateResultElement activate(AuthActivateElement authActivateElement) {
        return getAuth().activate(authActivateElement);
	}

	@POST
	@Path("check")
	public CheckAuthElement check(AuthAccessElement authAccessElement) {
		return new CheckAuthElement(getAuth().isAuthorized(authAccessElement.getAuthId(), authAccessElement.getAuthToken()));
	}

	private IAuth getAuth() {
		Injector injector = Guice.createInjector(new AuthModule());
		return injector.getInstance(IAuth.class);
	}
}
