package edu.service;

import edu.pojo.Response;
import edu.pojo.Status;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

@Component
public class ExelFileProcessorImpl implements ExelFileProcessor {
    @Override
    public Response process(String encodedString) throws IOException, InterruptedException {
        XSSFWorkbook workbook = new XSSFWorkbook(createFileFromEncodedString(encodedString));
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        List<Status> statuses = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Thread.sleep(5000);
            Iterator<Cell> cellIterator = rowIterator.next().iterator();
            Status st = createStatus(cellIterator);
            statuses.add(st);
        }

        Response resp = new Response();
        resp.setStatus("OK");

        if (statuses.size() == 0) {
            resp.setStatus("BAD");
        }

        return resp;
    }

    private Status createStatus(Iterator<Cell> cellIterator) {
        Status st = new Status();
        st.setDate(LocalDateTime.now());
        st.setIdUser(cellIterator.next().getNumericCellValue());
        st.setStatus(cellIterator.next().getStringCellValue());
        return st;
    }

    private FileInputStream createFileFromEncodedString(String encodedString) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String fileName = "temp.xlsx";
        File file = new File(fileName);
        FileUtils.writeByteArrayToFile(file, decodedBytes);
        return new FileInputStream(file);
    }
}
