package live.smoothing.batch.adapter;

import live.smoothing.batch.dto.mail.SendMailRequest;
import live.smoothing.batch.dto.mail.SendMailResponse;
import live.smoothing.batch.dto.mail.UploadFileRequest;
import live.smoothing.batch.dto.mail.UploadFileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "mailService", url = "${mail.url}")
public interface MailAdapter {

    @PostMapping("/email/v2.1/appKeys/${mail.appkey}/attachfile/binaryUpload")
    UploadFileResponse uploadFile(@RequestHeader("X-Secret-Key") String secret,
                                  @RequestBody UploadFileRequest request);

    @PostMapping("/email/v2.1/appKeys/${mail.appkey}/sender/mail")
    SendMailResponse sendMail(@RequestHeader("X-Secret-Key") String secret,
                              @RequestBody SendMailRequest request);
}
