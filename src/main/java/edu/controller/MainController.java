package edu.controller;

import edu.pojo.*;
import edu.service.ExelFileProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class MainController {

    private ExelFileProcessor exelFileProcessor;

    public MainController(ExelFileProcessor exelFileProcessor) {
        this.exelFileProcessor = exelFileProcessor;
    }

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
    public Response createBatchObjects(String code) throws IOException, InterruptedException {
        return exelFileProcessor.process(code);
    }
}
