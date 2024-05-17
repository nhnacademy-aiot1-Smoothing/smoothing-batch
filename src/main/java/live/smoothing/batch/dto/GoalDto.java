package live.smoothing.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalDto {
    private LocalDateTime goalDate;
    private Integer goalAmount;
    private Integer amount;
    private Integer unitPrice;
}
