package live.smoothing.batch.config;

import live.smoothing.batch.step.CheckThisMonthsGoalPassStep;
import live.smoothing.batch.step.UserPointStep;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final Step generateCsvReport;
    private final CheckThisMonthsGoalPassStep checkReachingThisMonthsTargetStep;
    private final UserPointStep userPointStep;
    private final Step sendReportEmail;

    @Bean("generateExcelReportAndSendEmailJob")
    public Job generateExcelReportAndSendEmailJob() {
        return jobBuilderFactory.get("generateCsvReportAndSendEmailJob")
                .start(generateCsvReport)
                .next(sendReportEmail)
                .build();
    }

    @Bean("checkReachingThisMonthsTargetJob")
    public Job checkReachingThisMonthsTargetJob() {
        return jobBuilderFactory.get("checkReachingThisMonthsTargetJob")
                .start(checkReachingThisMonthsTargetStep.getThisMonthsGoalStep())
                .next(userPointStep.getUsersStep())
                .build();
    }

}
