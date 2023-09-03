package guru.qa.niffler.test;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MethodDBTest {

    @DBUser
    @AllureId("108")
    @Test
    void verifyMethodUpdateUserDB(AuthUserEntity user) {
        AuthUserDAO authUserDAO = AuthUserDAO.getInstance();
        user.setPasswordUnEncoded("123456");
        authUserDAO.updateUser(user);
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPasswordUnEncoded())
                .statisticsIsPresent();
    }

    @DBUser
    @AllureId("109")
    @Test
    void verifyMethodGetUserDB(AuthUserEntity user) {
        AuthUserDAO authUserDAO = AuthUserDAO.getInstance();
        AuthUserEntity getUser = authUserDAO.getUserById(user.getId());

        Assertions.assertEquals(user.getUsername(),getUser.getUsername());
    }

    @DBUser(username = "Pavlik_1")
    @AllureId("110")
    @Test
    void verifyMethodUpdateAndGetUserInUserDataDB(AuthUserEntity user) {
        UserDataUserDAO userdataDAO = UserDataUserDAO.getInstance();
        UserDataUserEntity userData = userdataDAO.getUserDataByName(user.getUsername());
        userData.setCurrency(CurrencyValues.USD);
        userData.setFirstname("Pavel");
        userData.setSurname("Li");
        userData.setPhoto("its my photo jpg".getBytes());
        userdataDAO.updateUserData(userData);

        UserDataUserEntity updated = userdataDAO.getUserDataByName(user.getUsername());
        Assertions.assertEquals(userData.getSurname(),updated.getSurname());
    }
}
