package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.auth.AuthUserEntity;

import java.util.UUID;

public class AuthUserDAOHibernate extends JpaService implements AuthUserDAO {

    public AuthUserDAOHibernate() {
        super(EntityManagerFactoryProvider.INSTANCE.getDataSource(ServiceDB.AUTH).createEntityManager());
    }

    @Override
    public int createUser(AuthUserEntity user) {
        persist(user);
        return 0;
    }

    @Override
    public AuthUserEntity updateUser(AuthUserEntity user) {
        return merge(user);
    }

    @Override
    public void deleteUserById(UUID userId) {
        AuthUserEntity userEntity = getUserById(userId);
        remove(userEntity);
    }

    @Override
    public AuthUserEntity getUserById(UUID userId) {
        return em.createQuery("select u from AuthUserEntity u where u.id=:userId",
                        AuthUserEntity.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
