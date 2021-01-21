package main;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

public class WriteFile {

    // Метод для записи данных в файл
    public void jsonWriteFile(LinkedHashMap dateForWrite, String filename) throws IOException {
        FileWriter fileWriter = new FileWriter("report/" + filename);
        Gson gson = new Gson();
        String jsonDataForWrite = gson.toJson(dateForWrite, LinkedHashMap.class);
        fileWriter.write(jsonDataForWrite);
        System.out.println("Файл " + filename + " успешно создан");
        fileWriter.flush();
        fileWriter.close();
    }
}
