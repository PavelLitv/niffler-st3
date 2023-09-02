package guru.qa.niffler.test;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.DBUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MethodDBTest {

    @DBUser
    @AllureId("108")
    @Test
    void verifyMethodUpdateUserDB(UserEntity user) {
        AuthUserDAO authUserDAO = AuthUserDAO.getInstance();
        user.setPassword("123456");
        authUserDAO.updateUser(user);
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPassword())
                .statisticsIsPresent();
    }

    @DBUser
    @AllureId("109")
    @Test
    void verifyMethodGetUserDB(UserEntity user) {
        AuthUserDAO authUserDAO = AuthUserDAO.getInstance();
        UserEntity getUser = authUserDAO.getUserById(user.getId());

        Assertions.assertEquals(user.getUsername(),getUser.getUsername());
    }

    @DBUser(username = "Pavlik_1")
    @AllureId("110")
    @Test
    void verifyMethodUpdateAndGetUserInUserDataDB(UserEntity user) {
        UserJson userJson = new UserJson();
        userJson.setUsername(user.getUsername());
        userJson.setCurrency(CurrencyValues.USD);
        userJson.setFirstname("Pavel");
        userJson.setSurname("Li");
        userJson.setPhoto("its my photo jpg");

        UserDataUserDAO userdataDAO = UserDataUserDAO.getInstance();
        userdataDAO.updateUserByNameInUserData(userJson);

        UserDataEntity getUser = userdataDAO.getUserByNameInUserData(user.getUsername());
        Assertions.assertEquals(userJson.getSurname(),getUser.getSurname());
    }
}
