package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.AuthUserDAOSpringJdbc;
import guru.qa.niffler.db.model.auth.AuthUserEntity;

import java.util.UUID;

public interface AuthUserDAO {

    static AuthUserDAO getInstance() {
        AuthUserDAO dao;
        if ("hibernate".equals(System.getProperty("db.impl"))) {
            dao = new AuthUserDAOHibernate();
        } else if ("spring".equals(System.getProperty("db.impl"))) {
            dao = new AuthUserDAOSpringJdbc();
        } else {
            dao = new AuthUserDAOHibernate();
//            dao = new AuthUserDAOJdbc();
        }

        return dao;
    }

    int createUser(AuthUserEntity user);

    void deleteUserById(UUID userId);

    AuthUserEntity updateUser(AuthUserEntity user);

    AuthUserEntity getUserById(UUID userId);
}
