package live.smoothing.batch.config;

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
    private final Step sendReportEmail;

    @Bean("generateExcelReportAndSendEmailJob")
    public Job generateExcelReportAndSendEmailJob() {
        return jobBuilderFactory.get("generateCsvReportAndSendEmailJob")
                .start(generateCsvReport)
                .next(sendReportEmail)
                .build();
    }
}
