package tokenBasedAuthentification.config;

import com.google.inject.AbstractModule;
import tokenBasedAuthentification.dao.UserDao;
import tokenBasedAuthentification.dao.interfaces.IUserDao;
import tokenBasedAuthentification.useCase.auth.Auth;
import tokenBasedAuthentification.useCase.auth.interfaces.IAuth;

public class AuthModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IUserDao.class).to(UserDao.class);
        bind(IAuth.class).to(Auth.class);
    }
}
