package edu.service;

import edu.pojo.Response;
import edu.pojo.Status;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.util.Iterator;

public interface ExelFileProcessor {
    Response process(String encodedString) throws IOException, InterruptedException;

    Iterator<Row> createRowIt(String encodedString) throws IOException;
}
