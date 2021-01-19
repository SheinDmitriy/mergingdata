package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ParseCSVFile {

    public void parseLineFromCSV(String dataLine, LinkedHashMap dataForMerge) throws IOException {

            ArrayList<Integer> arrayTemp = new ArrayList<>();
            // Проверяем закоментированна строка или нет
            if(!dataLine.startsWith("#")){
                // Разделяем строку на ключ и значение
                String[] date = dataLine.split(",");
                String key = date[0].toLowerCase();
                // Заполняем массивы в HashMap соответственно ключу
                arrayTemp = (ArrayList<Integer>) dataForMerge.get(key);
                arrayTemp.add(Integer.parseInt(date[1]));
                dataForMerge.put(key,arrayTemp);
        }
    }
}
