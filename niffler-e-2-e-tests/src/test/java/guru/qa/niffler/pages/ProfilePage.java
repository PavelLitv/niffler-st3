package guru.qa.niffler.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

    private SelenideElement
            submit = $("button[type = 'submit']"),
            firstNameField = $("input[name = 'firstname']"),
            surnameField = $("input[name = 'surname']"),
            currencyField = $(".css-1dimb5e-singleValue");

    @Step("Create category")
    public ProfilePage createCategory(String categoryName) {
        $("input[name = 'category']").setValue(categoryName);
        $(byText("Create")).click();

        return this;
    }

    @Step("Set first name")
    public ProfilePage setFirstName(String firstName) {
        firstNameField.setValue(firstName);
        submit.click();

        return this;
    }

    @Step("Set surname")
    public ProfilePage setSurname(String surname) {
        surnameField.setValue(surname);
        submit.click();

        return this;
    }

    @Step("Set currency")
    public ProfilePage setCurrency(String currency) {
        $(".css-1hb7zxy-IndicatorsContainer").click();
        $(".css-1n6sfyn-MenuList").$$("div")
                .filter(Condition.text(currency))
                .first()
                .click();
        submit.click();

        return this;
    }

    @Step("Verify category is visible in all you spending categories")
    public void verifyCategoryIsVisible(String categoryName) {
        $$(".categories__item")
                .filter(Condition.text(categoryName))
                .shouldHave(CollectionCondition.size(1));
    }

    @Step("Verify first name")
    public void verifyFirstName(String firstName) {
        String actualFirstName = firstNameField.getValue();
        Assertions.assertEquals(firstName, actualFirstName);
    }

    @Step("Verify surname")
    public void verifySurname(String surname) {
        String actualSurname = surnameField.getValue();
        Assertions.assertEquals(surname, actualSurname);
    }

    @Step("Verify currency")
    public void verifyCurrency(String currency) {
        String actualCurrency = currencyField.getText();
        Assertions.assertEquals(currency, actualCurrency);
    }
}
