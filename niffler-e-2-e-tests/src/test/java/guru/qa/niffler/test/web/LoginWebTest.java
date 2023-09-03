package guru.qa.niffler.test.web;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class LoginWebTest extends BaseWebTest {

    @DBUser
    @AllureId("400")
    @Test
    void mainPageShouldBeVisibleAfterLogin(AuthUserEntity user) {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPasswordUnEncoded())
                .statisticsIsPresent();
    }

    @DBUser
    @AllureId("401")
    @Test
    void invalidLogin(AuthUserEntity user) {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .invalidLogin(user.getUsername(), "invalidPass")
                .checkErrorMessage(LoginPage.errorMessageInvalidLogin);
    }
}
