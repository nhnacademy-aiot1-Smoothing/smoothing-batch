package live.smoothing.batch.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class KwhDto {
    private String topic;
    private Instant time;
    private Double value;
}
