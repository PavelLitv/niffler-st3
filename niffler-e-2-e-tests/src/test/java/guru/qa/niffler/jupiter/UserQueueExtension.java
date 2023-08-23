package guru.qa.niffler.jupiter;

import guru.qa.niffler.jupiter.User.UserType;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import static guru.qa.niffler.jupiter.User.UserType.*;

public class UserQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    //dima, barsik - WITH_FRIENDS
    //bee, anna - INVITATION_SENT
    //valentin, pizzly - INVITATION_RECEIVED

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static Map<UserType, Queue<UserJson>> usersQueue = new ConcurrentHashMap<>();

    static {
        Queue<UserJson> userWithFriends = new ConcurrentLinkedDeque<>();
        userWithFriends.add(bindUser("dima", "12345"));
        userWithFriends.add(bindUser("barsik", "12345"));
        usersQueue.put(WITH_FRIENDS, userWithFriends);

        Queue<UserJson> userInvSent = new ConcurrentLinkedDeque<>();
        userInvSent.add(bindUser("bee", "12345"));
        userInvSent.add(bindUser("anna", "12345"));
        usersQueue.put(INVITATION_SENT, userInvSent);

        Queue<UserJson> userInvRec = new ConcurrentLinkedDeque<>();
        userInvRec.add(bindUser("valentin", "12345"));
        userInvRec.add(bindUser("pizzly", "12345"));
        usersQueue.put(INVITATION_RECEIVED, userInvRec);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Map<String, UserType> usersFromContext = getUsersFromContext(context);

        usersFromContext.forEach(
                (key, userType) -> {
                    Queue<UserJson> usersQueueByType = usersQueue.get(userType);
                    UserJson candidateForTest = null;
                    while (candidateForTest == null) {
                        candidateForTest = usersQueueByType.poll();
                    }
                    candidateForTest.setUserType(userType);
                    context.getStore(NAMESPACE).put(getAllureId(context) + key, candidateForTest);
                }
        );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<String, UserType> usersFromTest = getUsersFromContext(context);
        usersFromTest.forEach(
                (key, userType) -> {
                    UserJson userFromTest = context.getStore(NAMESPACE).get(getAllureId(context) + key, UserJson.class);
                    usersQueue.get(userFromTest.getUserType()).add(userFromTest);
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        UserType userType = parameterContext.getParameter().getAnnotation(User.class).userType();
        return extensionContext.getStore(NAMESPACE).get(getAllureId(extensionContext) + parameterContext.getDeclaringExecutable().toString() + userType, UserJson.class);
    }

    private String getAllureId(ExtensionContext context) {
        AllureId allureId = context.getRequiredTestMethod().getAnnotation(AllureId.class);
        if (allureId == null) {
            throw new IllegalStateException("Annotation AllureId must be present");
        }

        return allureId.value();
    }

    private static UserJson bindUser(String name, String password) {
        UserJson user = new UserJson();
        user.setUsername(name);
        user.setPassword(password);

        return user;
    }

    private Map<String, UserType> getUsersFromContext(ExtensionContext context) {
        Map<String, UserType> candidatesForTest = new HashMap<>();
        if (getUsersFromBeforeEach(context) != null) {
            candidatesForTest.putAll(getUsersFromBeforeEach(context));
        }
        candidatesForTest.putAll(getUsersFromTestMethod(context));

        return candidatesForTest;
    }

    private Map<String, UserType> getUsersFromTestMethod(ExtensionContext context) {
        Parameter[] parameters = context.getRequiredTestMethod().getParameters();

        return getUsersFromParams(parameters);
    }

    private Map<String, UserType> getUsersFromBeforeEach(ExtensionContext context) {
        Parameter[] parameters;
        Optional<Method> beforeEach = Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .findFirst();
        if (beforeEach.isPresent()) {
            parameters = beforeEach.get().getParameters();
            return getUsersFromParams(parameters);
        } else {
            return null;
        }
    }

    private Map<String, UserType> getUsersFromParams(Parameter[] parameters) {
        return Arrays.stream(parameters)
                .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
                .collect(Collectors.toMap(
                        (parameter) -> parameter.getDeclaringExecutable().toString() + parameter.getAnnotation(User.class).userType(),
                        (parameter) -> parameter.getAnnotation(User.class).userType()
                ));
    }
}
