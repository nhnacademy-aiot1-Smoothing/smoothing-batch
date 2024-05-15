package live.smoothing.batch.step.writer;

import live.smoothing.batch.dto.MonthKwhDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@StepScope
@Component
public class ConsoleWriter implements ItemWriter<MonthKwhDto> {

    @Override
    public void write(List<? extends MonthKwhDto> list) {
        list.forEach(monthKwhDto -> {
            log.info("=====sensorName=====");
            monthKwhDto.getSensorTopics().forEach(sensorTopicDto -> {
                log.info("SensorTopicDto: {}", sensorTopicDto.getSensorName());
            });
            log.info("=====startData=====");
            monthKwhDto.getStartData().forEach(kwhDto -> {
                log.info("time: {}", kwhDto.getTime());
                log.info("value: {}", kwhDto.getValue());
            });
            log.info("=====endData=====");
            monthKwhDto.getEndData().forEach(kwhDto -> {
                log.info("time: {}", kwhDto.getTime());
                log.info("value: {}", kwhDto.getValue());
            });
        });
    }
}
