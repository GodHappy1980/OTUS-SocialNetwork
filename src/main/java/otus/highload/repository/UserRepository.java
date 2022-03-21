package otus.highload.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import otus.highload.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository  {
    private static final String SQL_FIND_UER_BY_LOGIN = "SELECT * FROM accounts.user WHERE login = ?";

    private final JdbcTemplate jdbcTemplate;

    public Optional<User> findByLogin(String username) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                SQL_FIND_UER_BY_LOGIN,
                new BeanPropertyRowMapper<>(User.class),
                new Object[] {username}
        ));
    }

    //List<User>
}
