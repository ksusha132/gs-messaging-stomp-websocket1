package edu.service;

import edu.pojo.Response;
import edu.pojo.Status;
import edu.response.ResponseStatus;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

    Logger logger = LoggerFactory.getLogger(ExelFileProcessorImpl.class);

    @Override
    public Response process(String encodedString) throws IOException, InterruptedException {
        Iterator<Row> rowIterator = createRowIt(encodedString);
        List<Status> statuses = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Thread.sleep(5000);
            Iterator<Cell> cellIterator = rowIterator.next().iterator();
            Status st = createStatus(cellIterator);
            statuses.add(st);
        }

        Response resp = new Response();
        resp.setStatus(ResponseStatus.OK.getStatus());

        if (statuses.size() == 0) {
            resp.setStatus(ResponseStatus.BAD.getStatus());
        }

        return resp;
    }

    public Iterator<Row> createRowIt(String encodedString) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(createFileFromEncodedString(encodedString));
        XSSFSheet sheet = workbook.getSheetAt(0);
        logger.debug("Physical number of rows: " + sheet.getPhysicalNumberOfRows());
        return sheet.iterator();
    }

    private Status createStatus(Iterator<Cell> cellIterator) {
        Status st = new Status();
        st.setDate(LocalDateTime.now());
        st.setIdUser(cellIterator.next().getNumericCellValue());
        st.setStatus(cellIterator.next().getStringCellValue());
        return st;
    } // todo extract to separate service

    private FileInputStream createFileFromEncodedString(String encodedString) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String fileName = "temp.xlsx";
        File file = new File(fileName);
        FileUtils.writeByteArrayToFile(file, decodedBytes);
        return new FileInputStream(file);
    }
}
