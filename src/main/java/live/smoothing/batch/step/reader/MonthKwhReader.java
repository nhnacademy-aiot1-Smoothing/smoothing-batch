package live.smoothing.batch.step.reader;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import live.smoothing.batch.dto.GoalDto;
import live.smoothing.batch.dto.KwhDto;
import live.smoothing.batch.dto.MonthKwhDto;
import live.smoothing.batch.dto.SensorTopicDto;
import live.smoothing.batch.mapper.GoalDtoRowMapper;
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
import java.time.LocalDateTime;
import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class MonthKwhReader implements ItemReader<MonthKwhDto> {

    private static final String AGGREGATION_BUCKET = "aggregation";

    private final DataSource dataSource;
    private final InfluxDBClient aggregationInfluxClient;

    private boolean isRead = false;

    @Override
    public MonthKwhDto read() {
        if (isRead) {
            return null;
        }

        GoalDto goal = getGoal();
        List<SensorTopicDto> sensorTopics = getSensorTopics();
        String[] topics = getTopics(sensorTopics);

        List<KwhDto> kwhData = getMothKwhData(topics);

        isRead = true;

        return new MonthKwhDto(goal, sensorTopics, kwhData);
    }

    private GoalDto getGoal() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        LocalDateTime now = LocalDateTime.now();

        int year = now.minusMonths(1).getYear() == 12 ?
                now.getYear() - 1 :
                now.getYear();

        int month = now
                .minusMonths(1).getMonthValue();

        return jdbcTemplate.query(
                "SELECT *" +
                    "FROM goals " +
                    "WHERE YEAR(goal_date) = " + year +
                    " and MONTH(goal_date) = " + month,
                new GoalDtoRowMapper()
        )
        .stream().findFirst().orElse(null);
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
                .map(SensorTopicDto::getTopic)
                .toArray(String[]::new);
    }


    private List<KwhDto> getMothKwhData(String[] topics) {
        Flux fLux =
                FluxUtil.getKwhFromStart(
                        AGGREGATION_BUCKET,
                        "kwh_daily4",
                        TimeUtil.getRecentMonth(Instant.now()),
                        TimeUtil.getRecentDay(Instant.now()),
                        topics
                );

        return aggregationInfluxClient.getQueryApi().query(fLux.toString(), KwhDto.class);
    }
}
