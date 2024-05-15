package live.smoothing.batch.mapper;

import live.smoothing.batch.dto.SensorTopicDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SensorTopicDtoRowMapper implements RowMapper<SensorTopicDto> {

    @Override
    public SensorTopicDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        String sensorName = rs.getString("sensor_name");
        String topic = rs.getString("topic");
        return new SensorTopicDto(sensorName, topic);
    }
}
