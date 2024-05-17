package live.smoothing.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GoalDto {

    private Double goal_amount;
    private Double amount;
    private LocalDateTime goal_date;
}
