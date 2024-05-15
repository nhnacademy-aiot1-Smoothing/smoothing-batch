package live.smoothing.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthKwhDto {
    private List<SensorTopicDto> sensorTopics;
    private List<KwhDto> startData;
    private List<KwhDto> endData;
}
