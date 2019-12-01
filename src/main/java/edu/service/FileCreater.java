package edu.service;

import edu.pojo.RequestSpecFile;
import edu.pojo.Response;

import java.io.IOException;

public interface FileCreater {
    Response create(RequestSpecFile requestSpecFile) throws IOException;
}
