package live.smoothing.batch.dto.mail;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UploadFileResponse {
    Header header;
    UploadResponseBody body;
}