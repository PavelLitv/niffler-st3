package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.util.RandomData;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.*;

import java.util.ArrayList;
import java.util.Arrays;

public class UserEntityExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    private static AuthUserDAO authUserDAO = AuthUserDAO.getInstance();

    private static UserDataUserDAO userDataUserDAO = UserDataUserDAO.getInstance();

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserEntityExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        AuthUserEntity userEntity = new AuthUserEntity();
        if(annotation != null){
            userEntity.setUsername(annotation.username().equals("") ? RandomData.getName() : annotation.username());
            userEntity.setPasswordUnEncoded(annotation.password().equals("") ? RandomData.getPassword() : annotation.password());
            userEntity.setEnabled(true);
            userEntity.setAccountNonLocked(true);
            userEntity.setAccountNonExpired(true);
            userEntity.setCredentialsNonExpired(true);
            userEntity.setAuthorities(new ArrayList<>(Arrays.stream(Authority.values()).map(
                    authority -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(authority);
                        ae.setUser(userEntity);
                        return ae;
                    }
            ).toList()));

            authUserDAO.createUser(userEntity);

            UserDataUserEntity userData = new UserDataUserEntity();
            userData.setUsername(userEntity.getUsername());
            userData.setCurrency(CurrencyValues.RUB);
            userDataUserDAO.createUserInUserData(userData);

            context.getStore(NAMESPACE).put(getAllureId(context) + AuthUserEntity.class, userEntity);
            context.getStore(NAMESPACE).put(getAllureId(context) + UserDataUserEntity.class, userData);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(AuthUserEntity.class);
    }

    @Override
    public AuthUserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(getAllureId(extensionContext) + AuthUserEntity.class, AuthUserEntity.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        AuthUserEntity userFromTest = context.getStore(NAMESPACE).get(getAllureId(context) + AuthUserEntity.class, AuthUserEntity.class);
        UserDataUserEntity userDataFromTest = context.getStore(NAMESPACE).get(getAllureId(context) + UserDataUserEntity.class, UserDataUserEntity.class);
        authUserDAO.deleteUser(userFromTest);
        userDataUserDAO.deleteUserByNameInUserData(userDataFromTest);
    }

    private String getAllureId(ExtensionContext context) {
        AllureId allureId = context.getRequiredTestMethod().getAnnotation(AllureId.class);
        if (allureId == null) {
            throw new IllegalStateException("Annotation AllureId must be present");
        }

        return allureId.value();
    }
}
