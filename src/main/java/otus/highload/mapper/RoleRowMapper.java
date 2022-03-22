package otus.highload.mapper;

import org.springframework.jdbc.core.RowMapper;
import otus.highload.domain.Role;

import java.sql.ResultSet;
import java.sql.SQLException;


public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Role.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
