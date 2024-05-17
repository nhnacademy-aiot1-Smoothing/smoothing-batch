package live.smoothing.batch.step.processor;

import live.smoothing.batch.dto.KwhDto;
import live.smoothing.batch.dto.MonthKwhDto;
import live.smoothing.batch.step.writer.ReportSaveWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CreateExcelProcessor implements ItemProcessor<MonthKwhDto, XSSFWorkbook> {

    private StepExecution stepExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public XSSFWorkbook process(MonthKwhDto monthKwhDto) {
        Map<String, String> topicSensorNameMap = new HashMap<>();
        Map<String, List<KwhDto>> sensorNameKwhMap = new HashMap<>();

        monthKwhDto.getSensorTopics().forEach(sensorTopicDto -> {
            topicSensorNameMap.put(sensorTopicDto.getTopic(), sensorTopicDto.getSensorName());
            sensorNameKwhMap.put(sensorTopicDto.getSensorName(), new LinkedList<>());
        });

        List<KwhDto> kwhData = monthKwhDto.getKwhData();
        for (int i = kwhData.size()-1; i >= 1; i--) {
            String currentTopic = kwhData.get(i).getTopic();

            if (!kwhData.get(i).getTopic()
                    .equals(kwhData.get(i-1).getTopic())) {
                continue;
            }

            sensorNameKwhMap.get(
                    topicSensorNameMap.get(currentTopic)
            )
            .add(0, new KwhDto(
                    kwhData.get(i).getTopic(),
                    kwhData.get(i-1).getTime(),
                    kwhData.get(i).getValue() - kwhData.get(i-1).getValue()
            ));
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet summary = workbook.createSheet();
        XSSFSheet daily = workbook.createSheet();
        String date = getDateTimeString(LocalDateTime.now().minusMonths(1), "yyyy-MM");

        stepExecution.getJobExecution().getExecutionContext().put("date", date);

        workbook.setSheetName(0, "요약");
        workbook.setSheetName(1, "일별");

        XSSFCellStyle titleStyle = getTitleStyle(workbook);
        XSSFCellStyle theadStyle = getTheadStyle(workbook);

        // 요약 sheet
        // 제목 넣기
        int rowNum = 0;
        summary.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, 0, 3));
        XSSFRow titleRow = summary.createRow(rowNum);
        XSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(titleStyle);
        titleCell.setCellValue("요약");
        rowNum++;

        // 테이블 헤더 넣기
        XSSFRow theadRow = summary.createRow(++rowNum);
        createThead(theadStyle, theadRow, new String[]{"날짜", "목표량 (kwh)", "사용량 (kwh)", "요금 (₩)"});

        // 테이블 내용 채워 넣기
        Double amount = 0.0;
        for (Map.Entry<String, List<KwhDto>> entry : sensorNameKwhMap.entrySet()) {
            amount += getTotalAmount(entry.getValue());
        }
        Integer goalAmount = monthKwhDto.getGoal().getGoalAmount();
        Double cost = amount * monthKwhDto.getGoal().getUnitPrice();

        XSSFRow tbody = summary.createRow(++rowNum);
        tbody.createCell(0).setCellValue(date);
        tbody.createCell(1).setCellValue(goalAmount);
        tbody.createCell(2).setCellValue(amount);
        tbody.createCell(3).setCellValue(cost);

        rowNum += 4;
        summary.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, 0, 3));
        XSSFRow sensorSummaryTitleRow = summary.createRow(rowNum);
        XSSFCell sensorSummaryTitleCell = sensorSummaryTitleRow.createCell(0);
        sensorSummaryTitleCell.setCellStyle(titleStyle);
        sensorSummaryTitleCell.setCellValue("전력량");
        rowNum++;

        XSSFRow summaryTheadRow = summary.createRow(++rowNum);
        createThead(theadStyle, summaryTheadRow, new String[]{"날짜", "센서명", "사용량 (kwh)"});

        for (Map.Entry<String, List<KwhDto>> entry : sensorNameKwhMap.entrySet()) {
            XSSFRow summaryTbodyRow = summary.createRow(++rowNum);
            String sensorName = entry.getKey();
            Double sensorTotalAmount = getTotalAmount(entry.getValue());

            summaryTbodyRow.createCell(0).setCellValue(date);
            summaryTbodyRow.createCell(1).setCellValue(sensorName);
            summaryTbodyRow.createCell(2).setCellValue(sensorTotalAmount);
        }

        // 일별 sheet
        // 제목 넣기
        rowNum = 0;
        daily.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, 0, 3));
        XSSFRow dailyTitleRow = daily.createRow(rowNum);
        XSSFCell dailyTitleCell = dailyTitleRow.createCell(0);
        dailyTitleCell.setCellStyle(titleStyle);
        dailyTitleCell.setCellValue("일별");
        rowNum++;

        // 테이블 헤더 넣기
        XSSFRow dailyTheadRow = daily.createRow(++rowNum);
        createThead(theadStyle, dailyTheadRow, new String[]{"날짜", "센서명", "사용량 (kwh)"});

        for (Map.Entry<String, List<KwhDto>> entry : sensorNameKwhMap.entrySet()) {
            for (KwhDto kwh : entry.getValue()) {
                XSSFRow dailyTbodyRow = daily.createRow(++rowNum);
                dailyTbodyRow.createCell(0).setCellValue(getDateTimeString(LocalDateTime.ofInstant(kwh.getTime(), ZoneOffset.UTC), "yyyy-MM-dd"));
                dailyTbodyRow.createCell(1).setCellValue(entry.getKey());
                dailyTbodyRow.createCell(2).setCellValue(kwh.getValue());
            }
        }

        String fileName = "reportTemplate.html";

        // 템플릿 파일 읽기
        String templateContent = readResourceFile(fileName);

        // 값 치환
        if (templateContent != null) {
            String resultContent = templateContent
                    .replace("{date}", date)
                    .replace("{goalAmount}", goalAmount.toString())
                    .replace("{amount}", amount.toString())
                    .replace("{cost}", cost.toString());

            stepExecution.getJobExecution().getExecutionContext().put("reportContent", resultContent);
        }

        return workbook;
    }

    private XSSFCellStyle getTitleStyle(XSSFWorkbook workbook) {
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setWrapText(true);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        titleStyle.setFont(font);

        return titleStyle;
    }

    private XSSFCellStyle getTheadStyle(XSSFWorkbook workbook) {
        XSSFCellStyle theadStyle = workbook.createCellStyle();
        theadStyle.setWrapText(true);
        theadStyle.setAlignment(HorizontalAlignment.CENTER);
        theadStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        theadStyle.setBorderTop(BorderStyle.THIN);
        theadStyle.setBorderRight(BorderStyle.THIN);
        theadStyle.setBorderBottom(BorderStyle.THIN);
        theadStyle.setBorderLeft(BorderStyle.THIN);

        theadStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        theadStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return theadStyle;
    }

    private void createThead(XSSFCellStyle style, XSSFRow row, String[] thead) {
        for (int i = 0; i < thead.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(thead[i]);
            cell.setCellStyle(style);
        }
    }

    private Double getTotalAmount(List<KwhDto> kwhList) {
        return kwhList.stream()
                .map(KwhDto::getValue)
                .reduce(0.0, Double::sum);
    }

    private String getDateTimeString(LocalDateTime dateTime, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(dateTime);
    }

    private static String readResourceFile(String fileName) {
        ClassLoader classLoader = ReportSaveWriter.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (NullPointerException e) {
            log.error("파일을 찾을 수 없습니다: " + fileName);
        }
        return null;
    }
}