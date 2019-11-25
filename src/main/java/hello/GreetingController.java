package hello;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Iterator;

@Controller
public class GreetingController {

    @Autowired
    ExelFileProcessor exelFileProcessor;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        if (message.getName().equalsIgnoreCase("test")) {
            throw new RuntimeException("testing error queue");
        }
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageExceptionHandler
    @SendTo("/topic/failure")
    public Fail fail(Throwable exception) throws Exception {
        Fail fail = new Fail("fatal error with your fucking service! ");
        return new Fail("Looser! you got the error : " + HtmlUtils.htmlEscape(exception.getMessage()) + "!");
    }

    @MessageMapping("/batch")
    @SendTo("/topic/batch")
    public Status createBatchObjects(String code) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(code);
        String fileName = "temp.xlsx";
        File file = new File(fileName);
        FileUtils.writeByteArrayToFile(file, decodedBytes);
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Status st = new Status();
            st.setDate(LocalDateTime.now());
            st.setIdUser(rowIterator.next().getCell(0).getStringCellValue());
            st.setStatus(rowIterator.next().getCell(1).getStringCellValue());
            return st;
        }

        return null;
    }
}
