package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;

public class UserEntityExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    private static AuthUserDAO authUserDAO = new AuthUserDAOJdbc();

    private static UserDataUserDAO userDataUserDAO = new AuthUserDAOJdbc();

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserEntityExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(annotation.username());
        userEntity.setPassword(annotation.password());
        userEntity.setEnabled(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setAuthorities(Arrays.stream(Authority.values()).map(
                authority -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(authority);
                    return ae;
                }
        ).toList());

        context.getStore(NAMESPACE).put(getAllureId(context), userEntity);
        authUserDAO.createUser(userEntity);
        userDataUserDAO.createUserInUserData(userEntity);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(UserEntity.class);
    }

    @Override
    public UserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(getAllureId(extensionContext), UserEntity.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        UserEntity userFromTest = context.getStore(NAMESPACE).get(getAllureId(context), UserEntity.class);
        authUserDAO.deleteUserById(userFromTest.getId());
        userDataUserDAO.deleteUserByNameInUserData(userFromTest.getUsername());
    }

    private String getAllureId(ExtensionContext context) {
        AllureId allureId = context.getRequiredTestMethod().getAnnotation(AllureId.class);
        if (allureId == null) {
            throw new IllegalStateException("Annotation AllureId must be present");
        }

        return allureId.value();
    }
}
