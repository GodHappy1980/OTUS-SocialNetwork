package otus.highload.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import otus.highload.domain.Friendship;


import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendshipRepository {
    private final static String SQL_FIND_FRIENDS_BY_USER_ID =
                    "SELECT *" +
                    "  FROM accounts.friendship f" +
                    " WHERE f.src_user_id = ?";

    private static final String SQL_DELETE_FRIENDSHIP =
            "DELETE FROM accounts.friendship f " +
            " WHERE f.src_user_id = ? AND f.dst_user_id = ?";


    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert insertFriendship;

    @PostConstruct
    public void setup() {
        insertFriendship = new SimpleJdbcInsert(dataSource)
                .withSchemaName("accounts")
                .withTableName("friendship")
                .usingGeneratedKeyColumns("id");
    }

    public List<Friendship> findBySourceUserId(Integer sourceUserId) {
        return jdbcTemplate.query(
                SQL_FIND_FRIENDS_BY_USER_ID,
                new BeanPropertyRowMapper<>(Friendship.class),
                new Object[]{sourceUserId}
        );
    }

    public Friendship create(Friendship newFriendship) {
        Number id = insertFriendship.executeAndReturnKey(new BeanPropertySqlParameterSource(newFriendship));

        return Friendship.builder()
                .id(id.intValue())
                .srcUserId(newFriendship.getSrcUserId())
                .dstUserId(newFriendship.getDstUserId())
                .build();
    }

    public int deleteFriendship(Integer srcUserId, Integer dstUserId) {
        return jdbcTemplate.update(SQL_DELETE_FRIENDSHIP, new Object[]{srcUserId, dstUserId});
    }

    public boolean insertFriendship(Integer srcUserId, Integer dstUserId) {
        Number id = insertFriendship.executeAndReturnKey(
                new BeanPropertySqlParameterSource(
                        Friendship.builder()
                                .srcUserId(srcUserId)
                                .dstUserId(dstUserId)
                                .status(Byte.valueOf((byte) 1))//TODO
                                .build())
        );

        return id != null;
    }
}
