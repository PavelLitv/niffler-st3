package guru.qa.niffler.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {

    @Step("Verify friend is present")
    public void friendIsPresent(){
        $$("tr")
                .filter(Condition.text("You are friends"))
                .shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    @Step("Verify pending invitation is present")
    public void pendingInvitationIsPresent() {
        $$("tr")
                .filter(Condition.text("Pending invitation"))
                .shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    @Step("Verify invitation received is present")
    public void invitationReceivedIsPresent() {
        $$("div[data-tooltip-id = 'submit-invitation']")
                .shouldHave(CollectionCondition.sizeGreaterThan(0));
    }
}