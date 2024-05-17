package live.smoothing.batch.dto.mail;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Header {
    private Boolean isSuccessful;
    private Integer resultCode;
    private String resultMessage;
}