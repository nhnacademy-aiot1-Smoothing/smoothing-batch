package live.smoothing.batch.step;

import live.smoothing.batch.dto.GoalDto;
import live.smoothing.batch.mapper.GoalRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CheckThisMonthsGoalPassStep {

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Bean
    @JobScope
    public Step getThisMonthsGoalStep() {
        return stepBuilderFactory.get("getThisMonthsGoalStep")
                .tasklet(checkThisMonthsGoalPassTasklet())
                .build();

    }

    @Bean
    public Tasklet checkThisMonthsGoalPassTasklet() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return (contribution, chunkContext) -> {
            List<GoalDto> goalDtoList = jdbcTemplate.query("SELECT goal_amount,amount,goal_date  FROM goals WHERE YEAR(goal_date) = YEAR(CURDATE()) AND MONTH(goal_date) = MONTH(CURDATE() - INTERVAL 1 MONTH)", new GoalRowMapper());
            if (goalDtoList.size() > 0) {
                GoalDto goalDto = goalDtoList.get(0);
                if (goalDto.getGoal_amount() >= goalDto.getAmount()) {
                    chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("CONTINUABLE", "CONTINUABLE");
                    return RepeatStatus.FINISHED;
                }
            }
            throw new Exception("Goal is not reached");
        };
    }
}
