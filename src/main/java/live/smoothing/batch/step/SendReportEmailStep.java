package live.smoothing.batch.step;

import live.smoothing.batch.dto.UserInfo;
import live.smoothing.batch.step.writer.MailSendWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SendReportEmailStep {

    private final DataSource dataSource;
    private final StepBuilderFactory stepBuilderFactory;
    private final MailSendWriter mailSendWriter;

    @Bean
    @JobScope
    public Step sendReportEmail() {
        return stepBuilderFactory.get("sendReportEmail")
                .<UserInfo, UserInfo>chunk(100)
                .reader(reader())
                .writer(mailSendWriter)
                .build();
    }

    @Bean("userInfoReader")
    @StepScope
    public JdbcCursorItemReader<UserInfo> reader() {
        return new JdbcCursorItemReaderBuilder<UserInfo>()
                .name("userInfoReader")
                .dataSource(dataSource)
                .sql("SELECT user_name, user_email FROM users WHERE user_state = 'ACTIVE'")
                .rowMapper((rs, rowNum) -> new UserInfo(rs.getString("user_name"), rs.getString("user_email")))
                .build();
    }
}

