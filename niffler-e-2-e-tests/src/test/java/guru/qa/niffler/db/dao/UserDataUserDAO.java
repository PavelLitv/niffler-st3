package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.dao.impl.AuthUserDAOSpringJdbc;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public interface UserDataUserDAO {

    static UserDataUserDAO getInstance() {
        UserDataUserDAO dao;
        if ("hibernate".equals(System.getProperty("db.impl"))) {
            dao = new UserdataUserDAOHibernate();
        } else if ("spring".equals(System.getProperty("db.impl"))) {
            dao = new AuthUserDAOSpringJdbc();
        } else {
            dao = new UserdataUserDAOHibernate();
//            dao = new AuthUserDAOJdbc();
        }

        return dao;
    }

    int createUserInUserData(UserDataUserEntity userdata);

    void deleteUserByNameInUserData(UserDataUserEntity userdata);

    void updateUserByNameInUserData(UserDataUserEntity userdata);

    UserDataUserEntity getUserByNameInUserData(String username);
}
