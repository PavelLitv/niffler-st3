package guru.qa.niffler.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    @Step("Select spending")
    public MainPage selectSpending(String containsText) {
        getRowWithSpending(containsText)
                .$("td")
                .scrollTo()
                .click();

        return this;
    }

    @Step("Click Delete selected")
    public MainPage removeSelectedSpending() {
        $(byText("Delete selected")).click();

        return this;
    }

    @Step("Go to friends page")
    public FriendsPage goToFriendsPage() {
        $("a[href='/friends']").click();

        return new FriendsPage();
    }

    @Step("Go to all people page")
    public AllPeoplePage goToAllPeoplePage() {
        $("a[href='/people']").click();

        return new AllPeoplePage();
    }

    @Step("Verify spending {spendName} is present, expected result - {isPresent}")
    public void spendingIsPresent(String spendName, boolean isPresent) {
        if (isPresent) {
            getRowWithSpending(spendName).should(exist);
        } else {
            getRowWithSpending(spendName).should(not(exist));
        }
    }

    @Step("Verify icon invitation received is present")
    public void iconInvitationReceivedIsPresent() {
        $$("[class = 'header__sign']")
                .shouldHave(CollectionCondition.size(1));
    }

    @Step("Verify block statistics is present")
    public void statisticsIsPresent() {
        $(".main-content__section-stats").should(visible);
    }

    private SelenideElement getRowWithSpending(String containsText) {
        return $(".spendings__content tbody")
                .$$("tr")
                .find(text(containsText));
    }
}
