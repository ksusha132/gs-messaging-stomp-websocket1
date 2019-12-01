package edu.service;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class BeanService {
    private HashMap<String, String> beanMap = new HashMap<>();

    @PostConstruct
    public void fillInMap() {
        beanMap.put("json", "jsonFileCreater");
        beanMap.put("csv", "csvFileCreater");
    }

    public String getBeanName(String key) {
        return beanMap.get(key);
    }
}
