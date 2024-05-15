package live.smoothing.batch.step.reader;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import live.smoothing.batch.dto.KwhDto;
import live.smoothing.batch.dto.MonthKwhDto;
import live.smoothing.batch.dto.SensorTopicDto;
import live.smoothing.batch.mapper.SensorTopicDtoRowMapper;
import live.smoothing.batch.util.FluxUtil;
import live.smoothing.batch.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class MonthKwhReader implements ItemReader<MonthKwhDto> {

    private static final String RAW_BUCKET = "powermetrics_data";

    private final DataSource dataSource;
    private final InfluxDBClient rawInfluxClient;

    private boolean isRead = false;

    @Override
    public MonthKwhDto read() {
        if (isRead) {
            return null;
        }

        List<SensorTopicDto> sensorTopics = getSensorTopics();
        String[] topics = getTopics(sensorTopics);

        List<KwhDto> startData = getStartData(topics);
        List<KwhDto> endData = getLastData(topics);

        isRead = true;

        return new MonthKwhDto(sensorTopics, startData, endData);
    }

    private List<SensorTopicDto> getSensorTopics() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT s.sensor_name, t.topic " +
                    "FROM topics t " +
                    "JOIN sensors s " +
                    "ON t.sensor_id = s.sensor_id " +
                    "WHERE t.topic_type = '전력량'",
                new SensorTopicDtoRowMapper()
        );
    }

    private String[] getTopics(List<SensorTopicDto> sensorTopics) {
        return sensorTopics.stream()
                .map(SensorTopicDto::getTopics)
                .toArray(String[]::new);
    }

    private List<KwhDto> getStartData(String[] topics) {
        Flux fLux =
                FluxUtil.getFirstKwhFromStart(
                        RAW_BUCKET,
                        "mqtt_consumer",
                        TimeUtil.getRecentMonth(Instant.now()),
                        topics
                );

        return rawInfluxClient.getQueryApi().query(fLux.toString(), KwhDto.class);
    }

    private List<KwhDto> getLastData(String[] topics) {
        Flux fLux =
                FluxUtil.getLastKwhBetweenRange(
                        RAW_BUCKET,
                        "mqtt_consumer",
                        Instant.now().minus(10, ChronoUnit.MINUTES),
                        Instant.now(),
//                        TimeUtil.getRecentMonth(Instant.now()),
                        topics
                );

        return rawInfluxClient.getQueryApi().query(fLux.toString(), KwhDto.class);
    }
}
