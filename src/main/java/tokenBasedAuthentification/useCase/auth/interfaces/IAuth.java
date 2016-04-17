package tokenBasedAuthentification.useCase.auth.interfaces;

import tokenBasedAuthentification.dto.*;

public interface IAuth {

    AuthAccessElement login(AuthLoginElement loginElement);

    boolean isAuthorized(String email, String authToken);

    RegisterResultElement register(AuthRegisterElement registerElement);

    ActivateResultElement activate(AuthActivateElement authActivateElement);

    void deleteUser(AuthLoginElement loginElement);
}
