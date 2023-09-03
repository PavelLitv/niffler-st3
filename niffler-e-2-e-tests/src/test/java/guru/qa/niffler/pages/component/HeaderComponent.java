package guru.qa.niffler.pages.component;

import guru.qa.niffler.pages.AllPeoplePage;
import guru.qa.niffler.pages.FriendsPage;
import guru.qa.niffler.pages.ProfilePage;

import static com.codeborne.selenide.Selenide.$;

public class HeaderComponent {

    public FriendsPage goToFriendsPage() {
        $("a[href='/friends']").click();

        return new FriendsPage();
    }

    public AllPeoplePage goToAllPeoplePage() {
        $("a[href='/people']").click();

        return new AllPeoplePage();
    }

    public ProfilePage goToProfile() {
        $("a[href='/profile']").click();

        return new ProfilePage();
    }
}
