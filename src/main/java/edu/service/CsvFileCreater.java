package edu.service;

import edu.pojo.RequestSpecFile;
import edu.pojo.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CsvFileCreater implements FileCreater {
    @Override
    public Response create(RequestSpecFile requestSpecFile) throws IOException {
        return null;
    }
}
