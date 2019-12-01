package edu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pojo.*;
import edu.service.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

@Controller
public class MainController implements ApplicationContextAware {

    private ExelFileProcessor exelFileProcessor;
    private ApplicationContext applicationContext;
    private BeanService beanService;

    public MainController(ExelFileProcessor exelFileProcessor, BeanService beanService) {
        this.exelFileProcessor = exelFileProcessor;
        this.beanService = beanService;
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
        Fail fail = new Fail("Fatal error with your service! ");
        return new Fail("You got the error : " + HtmlUtils.htmlEscape(exception.getMessage()) + "!");
    }

    @MessageMapping("/batch")
    @SendTo("/topic/batch")
    public Response createBatchObjects(String code) throws IOException, InterruptedException {
        return exelFileProcessor.process(code);
    }

    @MessageMapping("/file")
    @SendTo("/topic/file")
    public Response createFile(String jsonStr) throws IOException, InterruptedException {
        RequestSpecFile request = new ObjectMapper().readValue(jsonStr, RequestSpecFile.class);
        FileCreater creater = (FileCreater) applicationContext.getBean("jsonFileCreater");

        if (request.getType().equalsIgnoreCase("csv")) {
            String beanName = beanService.getBeanName(request.getType());
            creater = (CsvFileCreater) applicationContext.getBean(beanName);
        }

        return creater.create(request);
    }

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
