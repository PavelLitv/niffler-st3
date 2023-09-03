package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.pages.ProfilePage;
import guru.qa.niffler.pages.WelcomePage;
import guru.qa.niffler.util.RandomData;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class ProfileWebTest extends BaseWebTest{

    @DBUser
    @AllureId("500")
    @Test
    void createdCategoryVisibleInAllYourCategories(AuthUserEntity user) {
        String categoryName = RandomData.getBeer();
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPasswordUnEncoded())
                .goToProfile()
                .createCategory(categoryName)
                .verifyCategoryIsVisible(categoryName);
    }

    @DBUser
    @AllureId("501")
    @Test
    void enteredFirstNameIsPreserved(AuthUserEntity user) {
        String firstName = RandomData.getName();
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPasswordUnEncoded())
                .goToProfile()
                .setFirstName(firstName);
        Selenide.refresh();
        new ProfilePage()
                .verifyFirstName(firstName);
    }

    @DBUser
    @AllureId("502")
    @Test
    void enteredSurNameIsPreserved(AuthUserEntity user) {
        String surname = RandomData.getName();
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPasswordUnEncoded())
                .goToProfile()
                .setSurname(surname);
        Selenide.refresh();
        new ProfilePage()
                .verifySurname(surname);
    }

    @DBUser
    @AllureId("503")
    @Test
    void enteredCurrencyIsPreserved(AuthUserEntity user) throws InterruptedException {
        String currency = "EUR";
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(user.getUsername(), user.getPasswordUnEncoded())
                .goToProfile()
                .setCurrency(currency);
        Selenide.refresh();
        new ProfilePage()
                .verifyCurrency(currency);
    }
}
