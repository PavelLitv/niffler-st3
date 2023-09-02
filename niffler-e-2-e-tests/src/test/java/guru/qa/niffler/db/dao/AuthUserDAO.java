package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public interface AuthUserDAO {

    static AuthUserDAO getInstance() {
        AuthUserDAO dao;
        if ("hibernate".equals(System.getProperty("db.impl"))) {
            dao = new AuthUserDAOHibernate();
        } else if ("spring".equals(System.getProperty("db.impl"))) {
            dao = new AuthUserDAOSpringJdbc();
        } else {
            dao = new AuthUserDAOJdbc();
        }

        return dao;
    }

    PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    int createUser(UserEntity user);

    void deleteUserById(UUID userId);

    UserEntity updateUser(UserEntity user);

    UserEntity getUserById(UUID userId);
}
