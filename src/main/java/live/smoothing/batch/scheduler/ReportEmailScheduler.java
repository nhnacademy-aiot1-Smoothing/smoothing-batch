package live.smoothing.batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ReportEmailScheduler {

    private final Job generateExcelReportAndSendEmailJob;
    private final JobLauncher jobLauncher;
    private static final String ONE_AM_OF_EVERY_MONTH = "0 1 1 * *";

    @Scheduled(cron = ONE_AM_OF_EVERY_MONTH)
    public void schedule() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime targetDate = startDate.minusMonths(1);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("startDate", startDate.format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .addString("targetDate", targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .toJobParameters();

        try {
            jobLauncher.run(generateExcelReportAndSendEmailJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
