package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

public class FriendWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(userForTest.getUsername(), userForTest.getPassword());
    }

    @Test
    @AllureId("101")
    void friendShouldBeDisplayedInFriendsPage() {
        new MainPage()
                .goToFriendsPage()
                .friendIsPresent();
    }

    @Test
    @AllureId("102")
    void friendShouldBeDisplayedInAllPeoplePage() {
        new MainPage()
                .goToAllPeoplePage()
                .friendIsPresent();
    }
}
