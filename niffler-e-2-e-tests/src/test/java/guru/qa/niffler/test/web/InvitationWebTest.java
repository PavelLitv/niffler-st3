package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;

public class InvitationWebTest extends BaseWebTest {

    @Test
    @AllureId("300")
    void pendingInvitationShouldBeDisplayedInAllPeoplePage(@User(userType = INVITATION_SENT) UserJson userInvSent) {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(userInvSent.getUsername(), userInvSent.getPassword())
                .goToAllPeoplePage()
                .pendingInvitationIsPresent();
    }

    @Test
    @AllureId("301")
    void invitationReceivedShouldBeDisplayedInAllPeoplePage(@User(userType = INVITATION_RECEIVED) UserJson userInvRec) {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(userInvRec.getUsername(), userInvRec.getPassword())
                .goToAllPeoplePage()
                .invitationReceivedIsPresent();
    }

    @Test
    @AllureId("302")
    void invitationReceivedShouldBeDisplayedInFriendsPage(@User(userType = INVITATION_RECEIVED) UserJson userInvRec) {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(userInvRec.getUsername(), userInvRec.getPassword())
                .goToFriendsPage()
                .invitationReceivedIsPresent();
    }

    @Test
    @AllureId("303")
    void iconInvitationReceivedShouldBeDisplayedInMainPage(@User(userType = INVITATION_RECEIVED) UserJson userInvRec) {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(userInvRec.getUsername(), userInvRec.getPassword())
                .iconInvitationReceivedIsPresent();
    }
}
