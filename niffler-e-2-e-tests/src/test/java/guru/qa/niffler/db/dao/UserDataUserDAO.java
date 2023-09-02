package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.model.UserJson;

public interface UserDataUserDAO {

    static UserDataUserDAO getInstance() {
        UserDataUserDAO dao;
        if ("hibernate".equals(System.getProperty("db.impl"))) {
            dao = new AuthUserDAOHibernate();
        } else if ("spring".equals(System.getProperty("db.impl"))) {
            dao = new AuthUserDAOSpringJdbc();
        } else {
            dao = new AuthUserDAOJdbc();
        }

        return dao;
    }

    int createUserInUserData(UserEntity user);

    void deleteUserByNameInUserData(String username);

    void updateUserByNameInUserData(UserJson userJson);

    UserDataEntity getUserByNameInUserData(String username);
}
