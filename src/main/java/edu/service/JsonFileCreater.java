package edu.service;

import edu.pojo.Response;
import edu.pojo.ResponseStatus;
import org.apache.poi.ss.usermodel.Row;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

@Service
public class JsonFileCreater implements FileCreater {

    Logger logger = LoggerFactory.getLogger(JsonFileCreater.class);

    private static final String ORIGIN_COUNTRY_NAME = "originCountryName";
    private static final String ORIGIN_COUNTRY_NAME_ENG = "originCountryNameEng";
    private static final String CODE = "code";

    private ExelFileProcessor exelFileProcessor;

    public JsonFileCreater(ExelFileProcessor exelFileProcessor) {
        this.exelFileProcessor = exelFileProcessor;
    }

    @Override
    public Response create(String encodedString) throws IOException {
        Iterator<Row> iterator = exelFileProcessor.createRowIt(encodedString);
        JSONArray countries = new JSONArray();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            JSONObject object = createJsonObject(row);
            logger.debug("Added object: " + object.toString());
            countries.add(object);
        }

        createFile(countries);

        Response response = new Response();
        response.setStatus(ResponseStatus.OK.getStatus());

        if (!isFileCreated("countries.json")) {
            response.setStatus(ResponseStatus.BAD.getStatus());
        }

        return response;
    }

    private JSONObject createJsonObject(Row row) {
        JSONObject object = new JSONObject();
        object.put(ORIGIN_COUNTRY_NAME, row.getCell(0).getStringCellValue());
        object.put(ORIGIN_COUNTRY_NAME_ENG, row.getCell(1).getStringCellValue());
        object.put(CODE, row.getCell(2).getNumericCellValue());
        return object;
    }

    private void createFile(JSONArray countries) {
        try (FileWriter file = new FileWriter("countries.json")) {
            file.write(countries.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFileCreated(String fileName) {
        return true;
    }
}
