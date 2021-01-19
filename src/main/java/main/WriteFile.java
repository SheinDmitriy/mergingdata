package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

public class WriteFile {

    // Метод для записи данных в файл
    public void jsonWriteFile(LinkedHashMap dateForWrite, String filename) throws IOException {
        FileWriter fileWriter = new FileWriter("report/" + filename);
        fileWriter.write(dateForWrite.toString());
        System.out.println("Файл " + filename + " успешно создан");
        fileWriter.flush();
        fileWriter.close();
    }
}
