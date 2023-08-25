package guru.qa.niffler.test;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.DBUser;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginTest extends BaseWebTest {

    @DBUser
    @AllureId("110")
    @Test
    void mainPageShouldBeVisibleAfterLogin(UserEntity user) {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPassword())
                .statisticsIsPresent();
    }

    @DBUser
    @AllureId("111")
    @Test
    void verifyMethodUpdateUserDB(UserEntity user) {
        AuthUserDAO authUserDAO = new AuthUserDAOJdbc();
        user.setPassword("123456");
        authUserDAO.updateUser(user);

        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPassword())
                .statisticsIsPresent();
    }

    @DBUser
    @AllureId("112")
    @Test
    void verifyMethodGetUserDB(UserEntity user) {
        AuthUserDAO authUserDAO = new AuthUserDAOJdbc();
        UserEntity getUser = authUserDAO.getUserById(user.getId());

        Assertions.assertEquals(user.getUsername(),getUser.getUsername());
    }
}
