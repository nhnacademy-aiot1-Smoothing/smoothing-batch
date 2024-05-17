package live.smoothing.batch.step.writer;

import live.smoothing.batch.adapter.MailAdapter;
import live.smoothing.batch.dto.mail.UploadFileRequest;
import live.smoothing.batch.dto.mail.UploadFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class ReportSaveWriter implements ItemWriter<XSSFWorkbook> {

    @Value("${mail.secret}")
    private String secret;

    private StepExecution stepExecution;
    private final MailAdapter mailAdapter;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(List<? extends XSSFWorkbook> list) {
        LocalDateTime now = LocalDateTime.now().minusMonths(1);
        String format = DateTimeFormatter.ofPattern("yyyy-MM").format(now);
        Path projectRoot = Paths.get(System.getProperty("user.dir"));
        Path reportDir = projectRoot.resolve("report");

        if (Files.notExists(reportDir)) {
            try {
                Files.createDirectory(reportDir);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패", e);
            }
        }

        list.stream().findFirst().ifPresent(workbook -> {

            Path filePath = reportDir.resolve(format + ".xlsx");
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                workbook.write(bos);

                byte[] bytes = bos.toByteArray();
                String fileName = format + ".xlsx";
                String createUser = "USER";

                UploadFileResponse uploadFileResponse = mailAdapter.uploadFile(secret, new UploadFileRequest(fileName, createUser, bytes));
                stepExecution.getJobExecution().getExecutionContext().put("fileId", uploadFileResponse.getBody().getData().getFileId());

                try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                    fos.write(bytes);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
