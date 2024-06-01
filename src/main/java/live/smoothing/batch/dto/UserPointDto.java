package live.smoothing.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPointDto {

    private String userId;
    private Integer point;
    private String point_usage;
}
