package edu.controller;

import edu.pojo.Fail;
import edu.pojo.Greeting;
import edu.pojo.HelloMessage;
import edu.pojo.Response;
import edu.service.ExelFileProcessor;
import edu.service.FileCreater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

@Controller
public class MainController {

    private ExelFileProcessor exelFileProcessor;
    @Qualifier("jsonFileCreater")
    private FileCreater fileCreater;

    public MainController(ExelFileProcessor exelFileProcessor, FileCreater jsonFileCreater) {
        this.exelFileProcessor = exelFileProcessor;
        this.fileCreater = jsonFileCreater;
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

    @MessageMapping("/file")
    @SendTo("/topic/file")
    public Response createFile(String code) throws IOException, InterruptedException {
        return fileCreater.create(code);
    }
}
