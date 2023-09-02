package guru.qa.niffler.test;

import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.DBUser;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class LoginTest extends BaseWebTest {

    @DBUser
    @AllureId("107")
    @Test
    void mainPageShouldBeVisibleAfterLogin(UserEntity user) {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPassword())
                .statisticsIsPresent();
    }
}
