package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpendingWebTest extends BaseWebTest{

    private static final String username = "dima";
    private static final String password = "12345";
    private static final String category = "Рыбалка";

    static {
        Configuration.browserSize = "1980x1024";
    }

    @BeforeEach
    void doLogin() {
        new WelcomePage()
                .openPage()
                .goToLogin()
                .signIn(username, password);
    }

    @Category(
            username = username,
            category = category
    )
    @Spend(
            username = username,
            description = "Рыбалка на Ладоге",
            category = category,
            amount = 14000.00,
            currency = CurrencyValues.RUB
    )
    @Test
    @AllureId("100")
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
        new MainPage()
                .selectSpending(createdSpend.getDescription())
                .removeSelectedSpending()
                .spendingIsPresent(createdSpend.getDescription(), false);
    }
}
