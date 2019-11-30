package edu.service;

import com.google.gson.JsonObject;
import edu.pojo.Response;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class JsonFileCreater implements FileCreater {

    private static final String ORIGIN_COUNTRY_NAME = "originCountryName";
    private static final String ORIGIN_COUNTRY_NAME_ENG = "originCountryNameEng";
    private static final String CODE = "code";

    @Autowired
    ExelFileProcessor exelFileProcessor;

    @Override
    public Response create(String encodedString) throws IOException {
        Iterator<Row> iterator = exelFileProcessor.createRowIt(encodedString);
        List<JsonObject> jsonObjectList = new ArrayList<>();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            JsonObject object = createJsonObject(row);
            jsonObjectList.add(object);
        }
        return new Response();
    }

    private JsonObject createJsonObject(Row row) {
        JsonObject object = new JsonObject();
        object.addProperty(ORIGIN_COUNTRY_NAME, row.getCell(0).getStringCellValue());
        object.addProperty(ORIGIN_COUNTRY_NAME_ENG, row.getCell(1).getStringCellValue());
        object.addProperty(CODE, row.getCell(2).getNumericCellValue());
        return object;

    }

    private void createFile(List<JsonObject> objectList) {
//
    }
}
