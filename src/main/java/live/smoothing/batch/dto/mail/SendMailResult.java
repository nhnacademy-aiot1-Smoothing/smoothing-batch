package live.smoothing.batch.dto.mail;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendMailResult {

    private String receiveMailAddr;
    private String receiveName;
    private String receiveType;
    private Integer resultCode;
    private String resultMessage;
}
