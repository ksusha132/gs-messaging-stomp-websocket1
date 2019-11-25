package edu.service;

import edu.pojo.Response;
import edu.pojo.Status;

import java.io.IOException;

public interface ExelFileProcessor {
    Response process(String encodedString) throws IOException, InterruptedException;
}
