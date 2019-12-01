package edu.service;

import edu.pojo.RequestSpecFile;
import edu.pojo.Response;
import edu.response.ResponseStatus;
import org.apache.poi.ss.usermodel.Row;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

@Service
public class JsonFileCreater implements FileCreater {

    private Logger logger = LoggerFactory.getLogger(JsonFileCreater.class);

    private static final String ORIGIN_COUNTRY_NAME = "originCountryName";
    private static final String ORIGIN_COUNTRY_NAME_ENG = "originCountryNameEng";
    private static final String CODE = "code";
    private static final String POSTFIX = ".json";

    private ExelFileProcessor exelFileProcessor;

    public JsonFileCreater(ExelFileProcessor exelFileProcessor) {
        this.exelFileProcessor = exelFileProcessor;
    }

    @Override
    public Response create(RequestSpecFile requestSpecFile) throws IOException {
        String fileName = requestSpecFile.getFileName().split("\\.")[0] + POSTFIX;
        logger.debug("File name is: " + fileName);
        Iterator<Row> iterator = exelFileProcessor.createRowIt(requestSpecFile.getCode());
        JSONArray objectsArr = new JSONArray();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            if (row.getCell(0) != null) {
                JSONObject object = createJsonObject(row);
                logger.debug("Added object: " + object.toString());
                objectsArr.add(object);
            }
        }

        createFile(objectsArr, fileName);

        Response response = new Response();
        response.setStatus(ResponseStatus.OK.getStatus());

        if (!isFileCreated(fileName)) {
            response.setStatus(ResponseStatus.BAD.getStatus());
        }

        return response;
    }

    private JSONObject createJsonObject(Row row) {
        JSONObject object = new JSONObject();
        object.put(ORIGIN_COUNTRY_NAME, row.getCell(0).getStringCellValue());
        object.put(ORIGIN_COUNTRY_NAME_ENG, row.getCell(1).getStringCellValue());
        object.put(CODE, (int) row.getCell(2).getNumericCellValue());
        return object;
    }

    private void createFile(JSONArray objectsArr, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(objectsArr.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFileCreated(String fileName) {
        return new File(fileName).isFile();
    }
}
