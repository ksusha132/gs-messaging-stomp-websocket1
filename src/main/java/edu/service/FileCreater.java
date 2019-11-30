package edu.service;

import edu.pojo.Response;

import java.io.IOException;

public interface FileCreater {
    Response create(String encodedString) throws IOException;
}
