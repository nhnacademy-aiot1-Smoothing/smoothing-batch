package live.smoothing.batch.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileRequest {

    private String fileName;
    private String createUser;
    private byte[] fileBody;
}
