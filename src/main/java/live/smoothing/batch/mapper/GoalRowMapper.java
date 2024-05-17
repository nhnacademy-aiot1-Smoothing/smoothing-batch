package live.smoothing.batch.mapper;

import live.smoothing.batch.dto.GoalDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GoalRowMapper implements RowMapper<GoalDto> {
    @Override
    public GoalDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GoalDto(rs.getDouble("goal_amount"),rs.getDouble("amount"),rs.getTimestamp("goal_date").toLocalDateTime());
    }
}
