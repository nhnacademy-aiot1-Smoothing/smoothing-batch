package live.smoothing.batch.step;

import live.smoothing.batch.dto.MonthKwhDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class GenerateExcelReportStep {

    private final ItemReader<MonthKwhDto> monthKwhReader;
    private final ItemWriter<XSSFWorkbook> itemWriter;
    private final ItemProcessor<MonthKwhDto, XSSFWorkbook> createExcelProcessor;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @JobScope
    public Step generateCsvReport() {
        return stepBuilderFactory.get("generateCsvReportAndSendEmailStep")
                .<MonthKwhDto, XSSFWorkbook>chunk(1)
                .reader(monthKwhReader)
                .processor(createExcelProcessor)
                .writer(itemWriter)
                .build();
    }
}
