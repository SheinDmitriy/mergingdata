package main;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyFileVisit extends SimpleFileVisitor<Path> {

    private static FileWriter jsonfile;

    private JSONObject jsonObjectV1 = new JSONObject();
    private JSONObject jsonObjectV2 = new JSONObject();
    private JSONObject jsonObjectV3 = new JSONObject();

    private static ArrayList<Integer> array1 = new ArrayList<>();
    private static ArrayList<Integer> array2 = new ArrayList<>();
    private static ArrayList<Integer> array3 = new ArrayList<>();
    private static ArrayList<Integer> array4 = new ArrayList<>();
    private static ArrayList<Integer> array5 = new ArrayList<>();
    private static ArrayList<Integer> array6 = new ArrayList<>();
    private static ArrayList<Integer> array7 = new ArrayList<>();

    // HashMap с заданными ключями и массивами данных для обработки
    private Map<String, ArrayList<Integer>> merge_key = new HashMap<>();

    // Заполняем HashMap клюяами и пустыми массивами
    public MyFileVisit() {
        merge_key.put("mark01", array1);
        merge_key.put("mark17", array2);
        merge_key.put("mark23", array3);
        merge_key.put("mark35", array4);
        merge_key.put("markfv", array5);
        merge_key.put("markfx", array6);
        merge_key.put("markft", array7);
    }

    // Метод который обходит и выболняет действия по очереди с каждым файлом в задвнной директории
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        String fileName = path.getFileName().toString();
        System.out.println("Обрабатываем файл: " + fileName);

        // Выявляем расширение файла
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
            // В зависимости от расширения запускаем соответсвующий парсер данных
            switch (fileName.substring(fileName.lastIndexOf(".")+1)){
                case "csv" :
                    parseCSV(path);
                    break;
                case "zip" :
                    parseZIP(path);
                    break;
            }
        } else {
            System.out.println("У файла " + fileName + " нет расширения");
        }
        return CONTINUE;
    }
    // Метод для парсера данных из ZIP файлв
    public void parseZIP(Path path) throws IOException{
        // Определяем zip файл как папку
        FileSystem fs = FileSystems.newFileSystem(path, null);
        Path zipPath = fs.getPath("/");

        // Запускаем обход по "zip-папке"
        try {
            Files.walkFileTree(zipPath, new MyFileVisit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для прасинга данных из cvs файлов
    public void parseCSV(Path path) throws IOException{
        // Считываем данные из файла в массив строк
        List<String> lines = Files.readAllLines(path);
        // Обходим каждую строку отдельно
        for (String s: lines) {
            ArrayList<Integer> arrayTemp = new ArrayList<>();
            // Проверяем закоментированна строка или нет
            if(!s.startsWith("#")){
                // Разделяем строку на ключ и значение
                String[] date = s.split(",");
                String key = date[0].toLowerCase();
                // Заполняем массивы в HashMap соответственно ключу
                arrayTemp = merge_key.get(key);
                arrayTemp.add(Integer.parseInt(date[1]));
                merge_key.put(key,arrayTemp);
            }
        }
        System.out.println(merge_key);
    }

    // Метод для выполнения действий после завершения обхода директории
    @Override
    public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
        // Если закончили обход "zip-папки" то ничего не делаем, просто завершаем обход
        if(path.toString().equals("/")){
            return TERMINATE;
        }
        // Обробатываем данные в массивах HashMap для формирования трех типов отчетов
        System.out.println("Формируем данные для отчета");
        // Обходим каждый элемент HashMap
        merge_key.forEach((k, v) ->{
            // Проверяем пустой ли массив
            if(!v.isEmpty()){
                // Сортируем данные в массиве для 3 отчета
                Collections.sort(v, Collections.reverseOrder());
                jsonObjectV3.put(k,v);
                // Обходим массивы и складываем значения внутри для отчета 1 и 2
                int count = 0;
                for (Integer s: v) {
                    count = count + s;
                }
                jsonObjectV1.put(k, count);
                jsonObjectV2.put(k, count);
            }else {
                // Добавляем пустые массивы для отчета 2
                jsonObjectV2.put(k, null);
            }
        });
        // Записываем данные в три разных файла
        System.out.println(jsonObjectV3);
        jsonWriteFile(jsonObjectV3, "report №3.txt");
        System.out.println(jsonObjectV1.toString());
        System.out.println("Формируем файл для отчета №1");
        jsonWriteFile(jsonObjectV1, "report №1.txt");
        System.out.println(jsonObjectV2.toString());
        System.out.println("Формируем файл для отчета №2");
        jsonWriteFile(jsonObjectV2, "report №2.txt");

        return TERMINATE;
    }

    // Метод для записи данных в файл
    private void jsonWriteFile(JSONObject jsonObject, String filename) throws IOException{
        jsonfile = new FileWriter("report/" + filename);
        jsonfile.write(jsonObject.toJSONString());
        System.out.println("Файл " + filename + " успешно создан");
        jsonfile.flush();
        jsonfile.close();
    }
}
