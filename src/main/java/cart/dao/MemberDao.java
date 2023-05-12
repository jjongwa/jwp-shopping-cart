package cart.dao;

import cart.entity.MemberEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MemberEntity> customerMapper
            = (resultSet, rowNum) -> new MemberEntity(
            resultSet.getInt("id"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    private final RowMapper<Boolean> booleanMapper = (resultSet, rowNum) -> resultSet.getBoolean("isExist");

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addMember(MemberEntity memberEntity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(memberEntity);
        Number id = simpleJdbcInsert.executeAndReturnKey(parameterSource);
        return id.intValue();
    }

    public List<MemberEntity> selectAllMembers() {
        String sql = "select id, email, password from member";
        return jdbcTemplate.query(sql, customerMapper);
    }

    public int findMemberId(String email, String password) {
        String sql = "select id " +
                "from member " +
                "where member.email = ? and member.password = ?";
        return jdbcTemplate.queryForObject(sql, int.class, email, password);
    }

    public boolean isMemberExist(String email, String password) {
        String sql = "select exists(" +
                "select * from member where member.email = ? and member.password = ?) as isExist";
        return jdbcTemplate.queryForObject(sql, booleanMapper, email, password);
    }

    public void deleteAllMembers() {
        String sql = "delete from member";
        jdbcTemplate.update(sql);
    }

}
