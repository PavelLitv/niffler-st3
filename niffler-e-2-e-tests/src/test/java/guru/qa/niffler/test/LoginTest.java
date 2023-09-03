package guru.qa.niffler.test;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class LoginTest extends BaseWebTest {

    @DBUser
    @AllureId("107")
    @Test
    void mainPageShouldBeVisibleAfterLogin(AuthUserEntity user) {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPasswordUnEncoded())
                .statisticsIsPresent();
    }
}
