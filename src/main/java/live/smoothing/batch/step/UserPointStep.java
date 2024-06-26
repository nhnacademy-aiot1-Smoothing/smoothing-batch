package live.smoothing.batch.step;

import live.smoothing.batch.dto.UserDto;
import live.smoothing.batch.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserPointStep {

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final Integer point = 1000;
    private final String point_usage = "적립";
    private final String user_status = "ACTIVE";

    @Bean
    @JobScope
    public Step getUsersStep() {
        return stepBuilderFactory.get("getUsersStep")
                .<UserDto, UserDto>chunk(500)
                .reader(reader())
                .writer(itemWriter())
                .build();
    }

    @Bean("userReader")
    public ItemReader<UserDto> reader() {
        JdbcCursorItemReader<UserDto> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT user_id FROM users WHERE user_state = '" + user_status + "'");
        reader.setRowMapper(new UserRowMapper());
        return reader;
    }

    @Bean
    public ItemWriter<UserDto> itemWriter() {
        return new JdbcBatchItemWriterBuilder<UserDto>()
                .dataSource(dataSource)
                .sql("INSERT INTO point_details (user_id, point_amount,point_usage,point_date) VALUES (:userId," + point + ",'" + point_usage + "',CURDATE())")
                .beanMapped()
                .build();
    }
}
