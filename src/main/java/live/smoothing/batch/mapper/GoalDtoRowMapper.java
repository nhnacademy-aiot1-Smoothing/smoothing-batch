package live.smoothing.batch.mapper;

import live.smoothing.batch.dto.GoalDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GoalDtoRowMapper implements RowMapper<GoalDto> {

    @Override
    public GoalDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GoalDto.builder()
                .goalDate(rs.getTimestamp("goal_date").toLocalDateTime())
                .goalAmount(rs.getInt("goal_amount"))
                .amount(rs.getInt("amount"))
                .unitPrice(rs.getInt("unit_price"))
                .build();
    }
}
