package live.smoothing.batch.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendMailRequest {

    private String senderAddress;
    private String title;
    private String body;
    private List<Integer> attachFileIdList;
    private List<Receiver> receiverList;
}
