package otus.highload.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import otus.highload.domain.Role;
import otus.highload.domain.User;
import otus.highload.mapper.RoleRowMapper;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class UserRepository  {
    private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM accounts.user WHERE login = ?";

    private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM accounts.user WHERE id = ?";

    private static final String SQL_FIND_ROLES_BY_USER_ID =
            "SELECT r.* " +
            "  FROM accounts.user_role ur" +
            " INNER JOIN accounts.role r ON r.id = ur.role_id" +
            " WHERE ur.user_id = ?";

    private static final String SQL_FIND_ROLE_BY_NAME = "SELECT * FROM accounts.role WHERE name = ?";

    private static final String SQL_FIND_ALL_USERS = "SELECT * FROM accounts.user";
    private static final String SQL_UPDATE_USER =
            "UPDATE accounts.user u" +
            "   SET u.first_name = ?" +
            "      ,u.last_name = ?" +
            "      ,u.gender = ?" +
            "      ,u.interests = ?" +
            "      ,u.city = ?" +
            "      ,u.login = ?" +
            "      ,u.password = ?" +
            "      ,u.enabled = ?" +
            " WHERE u.id = ?";

    private static final String SQL_FIND_FRIENDS_BY_USER_ID =
            "SELECT u.* " +
            "  FROM accounts.user u" +
            "      ,accounts.friendship f " +
            " WHERE f.src_user_id = ?" +
            "   AND f.dst_user_id = u.id";

    private final JdbcTemplate jdbcTemplate;

    private final DataSource dataSource;

    private SimpleJdbcInsert insertUser;

    private SimpleJdbcInsert insertUserRole;


    @PostConstruct
    public void setup() {
        insertUser = new SimpleJdbcInsert(dataSource)
                .withCatalogName("accounts")
                //.withSchemaName("accounts")
                .withTableName("accounts.user")
                .usingGeneratedKeyColumns("id");

        insertUserRole = new SimpleJdbcInsert(dataSource)
                .withCatalogName("accounts")
                //.withSchemaName("accounts")
                .withTableName("accounts.user_role")
                .usingGeneratedKeyColumns("id");
    }

    public Optional<User> findByLogin(String username) {
        List<User> users = jdbcTemplate.query(
                SQL_FIND_USER_BY_LOGIN,
                new BeanPropertyRowMapper<>(User.class),
                new Object[]{username}
        );

        Optional<User> res = Optional.ofNullable(DataAccessUtils.singleResult(users));

        return res;
    }

    public User create(final User userToCreate) throws SQLException {
        Number id = insertUser.executeAndReturnKey(new BeanPropertySqlParameterSource(userToCreate));

        User user = jdbcTemplate.queryForObject(
                SQL_FIND_USER_BY_ID,
                new BeanPropertyRowMapper<>(User.class),
                new Object[] {id}
        );

        return user;
    }

    public Optional<Role> grantRole(Number userId, String roleName) throws SQLException {
        Role role = DataAccessUtils.singleResult(jdbcTemplate.query(
                SQL_FIND_ROLE_BY_NAME,
                new BeanPropertyRowMapper<>(Role.class),
                new Object[] {roleName}
        ));

        if (role != null) {
            Number id = insertUserRole.executeAndReturnKey(
                    //Map.of("USER_ID", userId, "ROLE_ID", role.getId())
                    Stream.of(new Object[][] {
                            { "USER_ID", userId },
                            { "ROLE_ID", role.getId() },
                    }).collect(Collectors.toMap(data -> (String)data[0], data -> (Number)data[1]))
            );

            if (id == null)
                throw new SQLException("Could create link between user and role");
        }

        return Optional.ofNullable(role);
    }

    public List<User> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL_USERS, new BeanPropertyRowMapper<>(User.class));
    }

    public Optional<User> findById(Integer userId) {
        List<User> users = jdbcTemplate.query(
                SQL_FIND_USER_BY_ID,
                new BeanPropertyRowMapper<>(User.class),
                new Object[]{userId}
        );

        Optional<User> res = Optional.ofNullable(DataAccessUtils.singleResult(users));

        return res;
    }

    public Collection<Role> findRolesByUserId(Integer userId) {
        return jdbcTemplate.query(
                SQL_FIND_ROLES_BY_USER_ID,
                new RoleRowMapper(),
                new Object[] {userId}
        );
    }

    public boolean update(User user) {
        return jdbcTemplate.update(SQL_UPDATE_USER, new Object[] {
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getInterests(),
                user.getCity(),
                user.getLogin(),
                user.getPassword(),
                user.isEnabled(),
                user.getId()
        }) > 0;
    }

    public List<User> findFriendsById(Integer srcUserId) {
        return jdbcTemplate.query(
                SQL_FIND_FRIENDS_BY_USER_ID,
                new BeanPropertyRowMapper<>(User.class),
                new Object[]{srcUserId}
        );
    }
}
