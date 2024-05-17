package live.smoothing.batch.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Receiver {
    private String receiveMailAddr;
    private String receiveName;
    private String receiveType;
}
