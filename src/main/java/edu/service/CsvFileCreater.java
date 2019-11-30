package edu.service;

import edu.pojo.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CsvFileCreater implements FileCreater {
    @Override
    public Response create(String encodedString) throws IOException {
        return null;
    }
}
