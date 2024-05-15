package live.smoothing.batch.step;

import live.smoothing.batch.dto.MonthKwhDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class GenerateCsvReportAndSendEmailStep {

    private final ItemReader<MonthKwhDto> monthKwhReader;
    private final ItemWriter<MonthKwhDto> itemWriter;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @JobScope
    public Step generateCsvReport() {
        return stepBuilderFactory.get("generateCsvReportAndSendEmailStep")
                .<MonthKwhDto, MonthKwhDto>chunk(1)
                .reader(monthKwhReader)
                .writer(itemWriter)
                .build();
    }
}
