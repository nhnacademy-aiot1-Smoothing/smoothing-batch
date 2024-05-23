package live.smoothing.batch.config;

import live.smoothing.batch.step.CheckThisMonthsGoalPassStep;
import live.smoothing.batch.step.UserPointStep;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final Step generateCsvReport;
    private final CheckThisMonthsGoalPassStep checkReachingThisMonthsTargetStep;
    private final UserPointStep userPointStep;
    private final Step sendReportEmail;
    private final JobLauncher jobLauncher;

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

    @Scheduled(cron = "* * 1 1 * ?")
    public void runGenerateExcelReportAndSendEmailJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameter = new JobParametersBuilder()
                .addString("generateExcelReportAndSendEmailJob", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(generateExcelReportAndSendEmailJob(), jobParameter);
    }

    @Scheduled(cron = "* * 2 1 * ?")
    public void runCheckReachingThisMonthsTargetJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameter = new JobParametersBuilder()
                .addString("checkReachingThisMonthsTargetJob", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(checkReachingThisMonthsTargetJob(), jobParameter);
    }

}
