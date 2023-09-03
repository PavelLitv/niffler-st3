package guru.qa.niffler.db.sjdbc;

import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDataUserEntityRowMapper implements RowMapper<UserDataUserEntity> {

    public static UserDataUserEntityRowMapper instance = new UserDataUserEntityRowMapper();

    @Override
    public UserDataUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDataUserEntity user = new UserDataUserEntity();
        user.setId(rs.getObject("id", UUID.class));
        user.setUsername(rs.getString("username"));
        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        user.setFirstname(rs.getString("firstname"));
        user.setSurname(rs.getString("surname"));
        user.setPhoto(rs.getBytes("photo"));

        return user;
    }
}
