package otus.highload.mapper;

import org.springframework.jdbc.core.RowMapper;
import otus.highload.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        return User.builder()
                .id(rs.getInt("id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .gender(rs.getString("gender"))
                .interests(rs.getString("interests"))
                .city(rs.getString("city"))
                .login(rs.getString("login"))
                .password(rs.getString("password"))
                .build();
    }
}
