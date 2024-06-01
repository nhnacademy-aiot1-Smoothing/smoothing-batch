package live.smoothing.batch.step.writer;

import live.smoothing.batch.adapter.MailAdapter;
import live.smoothing.batch.dto.UserInfo;
import live.smoothing.batch.dto.mail.Receiver;
import live.smoothing.batch.dto.mail.SendMailRequest;
import live.smoothing.batch.dto.mail.SendMailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailSendWriter implements ItemWriter<UserInfo> {

    @Value("${mail.secret}")
    private String secret;

    private final MailAdapter mailAdapter;
    private StepExecution stepExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(List<? extends UserInfo> list) throws Exception {
        String date = stepExecution.getJobExecution().getExecutionContext().getString("date");
        String title = date + " 전력 사용량 보고서";
        String body = stepExecution.getJobExecution().getExecutionContext().getString("reportContent");
        List<Integer> fileIdList = List.of(Integer.parseInt(stepExecution.getJobExecution().getExecutionContext().getString("fileId")));

        List<Receiver> receivers = new ArrayList<>();

        for (UserInfo userInfo : list) {
            receivers.add(new Receiver(userInfo.getEmail(), userInfo.getName(), "MRT0"));
        }

        SendMailRequest request = new SendMailRequest("smoothing@smoothing.live", title, body, fileIdList, receivers);
        SendMailResponse sendMailResponse = mailAdapter.sendMail(secret, request);

        log.error("sendMailResponse: {}", sendMailResponse.getHeader().getResultMessage());
        log.error("sendMailResponse: {}", sendMailResponse.getHeader().getResultCode());
    }
}
