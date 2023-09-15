package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public static final String errorMessageInvalidLogin = "Неверные учетные данные пользователя";

    private SelenideElement
            userNameField = $("input[name = 'username']"),
            passwordField = $("input[name = 'password']"),
            submitButton = $("button[type = 'submit']"),
            errorMessage = $(".form__error");


    @Step("Sign in")
    public MainPage signIn(String userName, String password) {
        userNameField.setValue(userName);
        passwordField.setValue(password);
        submitButton.click();

        return new MainPage();
    }

    @Step("Sign in with not valid data")
    public LoginPage invalidLogin(String userName, String invalidPass) {
        userNameField.setValue(userName);
        passwordField.setValue(invalidPass);
        submitButton.click();

        return this;
    }

    @Step("Verify error message when entering incorrect data")
    public void checkErrorMessage(String errorMessageInvalidLogin) {
        String actualMessage = errorMessage.getText();
        Assertions.assertEquals(errorMessageInvalidLogin, actualMessage);
    }
}
