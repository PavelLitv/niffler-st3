package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public class AuthUserDAOHibernate implements AuthUserDAO, UserDataUserDAO {

    @Override
    public int createUser(UserEntity user) {
        return 0;
    }

    @Override
    public void deleteUserById(UUID userId) {

    }

    @Override
    public int updateUser(UserEntity user) {
        return 0;
    }

    @Override
    public UserEntity getUserById(UUID userId) {
        return null;
    }

    @Override
    public int createUserInUserData(UserEntity user) {
        return 0;
    }

    @Override
    public void deleteUserByNameInUserData(String username) {

    }
}