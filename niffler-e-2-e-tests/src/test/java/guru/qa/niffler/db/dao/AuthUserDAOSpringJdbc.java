package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.mapper.AuthorityEntityRowMapper;
import guru.qa.niffler.db.mapper.UserEntityRowMapper;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class AuthUserDAOSpringJdbc implements AuthUserDAO, UserDataUserDAO {

    private final TransactionTemplate authTtpl;
    private final TransactionTemplate userDataTtpl;
    private final JdbcTemplate authJdbcTemplate;
    private final JdbcTemplate userDataJdbcTemplate;

    public AuthUserDAOSpringJdbc() {
        JdbcTransactionManager authTm = new JdbcTransactionManager(
                DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH)
        );
        JdbcTransactionManager userDataTm = new JdbcTransactionManager(
                DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA)
        );

        this.authTtpl = new TransactionTemplate(authTm);
        this.userDataTtpl = new TransactionTemplate(userDataTm);
        this.authJdbcTemplate = new JdbcTemplate(authTm.getDataSource());
        this.userDataJdbcTemplate = new JdbcTemplate(userDataTm.getDataSource());
    }

    @Override
    public int createUser(UserEntity user) {
        return authTtpl.execute(status -> {
            KeyHolder kh = new GeneratedKeyHolder();
            authJdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, pe.encode(user.getPassword()));
                ps.setBoolean(3, user.getEnabled());
                ps.setBoolean(4, user.getAccountNonExpired());
                ps.setBoolean(5, user.getAccountNonLocked());
                ps.setBoolean(6, user.getCredentialsNonExpired());
                return ps;
            }, kh);
            UUID userId = (UUID) kh.getKeyList().get(0).get("id");
            authJdbcTemplate.batchUpdate(
                    "INSERT INTO authorities (user_id, authority) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setObject(1, userId);
                            ps.setObject(2, Authority.values()[i].name());
                        }

                        @Override
                        public int getBatchSize() {
                            return Authority.values().length;
                        }
                    });
            return 0;
        });
    }

    @Override
    public void deleteUserById(UUID userId) {
        authTtpl.execute(status -> {
            authJdbcTemplate.update(con -> {
                PreparedStatement authorityPs = con.prepareStatement("DELETE from authorities WHERE user_id = ?");
                authorityPs.setObject(1, userId);
                return authorityPs;
            });
            authJdbcTemplate.update(con -> {
                PreparedStatement usersPs = con.prepareStatement("DELETE from users WHERE id = ?");
                usersPs.setObject(1, userId);
                return usersPs;
            });
            return 0;
        });
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        authJdbcTemplate.update("UPDATE users SET password = ?, enabled = ?, account_non_expired = ?," +
                        "account_non_locked = ? WHERE id = ?",
                pe.encode(user.getPassword()), user.getEnabled(), user.getAccountNonExpired(),
                user.getAccountNonLocked(), user.getId()
        );
        return getUserById(user.getId());
    }

    @Override
    public UserEntity getUserById(UUID userId) {
        UserEntity user =
                authJdbcTemplate.queryForObject(
                        "SELECT * FROM users WHERE id = ?",
                        UserEntityRowMapper.instance,
                        userId
                );
        user.setAuthorities(
                authJdbcTemplate.query(
                        "SELECT * FROM authorities WHERE user_id = ?",
                        AuthorityEntityRowMapper.instance,
                        userId
                ));
        user.getAuthorities().forEach(
                entity -> entity.setUser(user)
        );
        return user;
    }

    @Override
    public int createUserInUserData(UserEntity user) {
        return userDataJdbcTemplate.update(
                "INSERT INTO users (username, currency) VALUES (?, ?)",
                user.getUsername(), CurrencyValues.RUB.name()
        );
    }

    @Override
    public void deleteUserByNameInUserData(String username) {
        userDataJdbcTemplate.update(
                "DELETE FROM users WHERE username = ?", username
        );
    }

    @Override
    public void updateUserByNameInUserData(UserJson userJson) {

    }

    @Override
    public UserDataEntity getUserByNameInUserData(String username) {
        return null;
    }
}
