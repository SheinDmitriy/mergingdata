package main;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyFileVisit extends SimpleFileVisitor<Path> {

    private WriteFile writeFile = new WriteFile();
    private ParseCSVFile parseCSVFile = new ParseCSVFile();

    private LinkedHashMap<String, Integer> dataForReport1 = new LinkedHashMap<>();
    private LinkedHashMap<String, Integer> dataForReport2 = new LinkedHashMap<>();
    private LinkedHashMap<String, ArrayList<Integer>> dataForReport3 = new LinkedHashMap<>();

    private static ArrayList<Integer> arrayDataMark01 = new ArrayList<>();
    private static ArrayList<Integer> arrayDataMark17 = new ArrayList<>();
    private static ArrayList<Integer> arrayDataMark23 = new ArrayList<>();
    private static ArrayList<Integer> arrayDataMark35 = new ArrayList<>();
    private static ArrayList<Integer> arrayDataMarkFv = new ArrayList<>();
    private static ArrayList<Integer> arrayDataMarkFx = new ArrayList<>();
    private static ArrayList<Integer> arrayDataMarkFt = new ArrayList<>();

    // HashMap с заданными ключями и массивами данных для обработки
    private LinkedHashMap<String, ArrayList<Integer>> dataForMerge = new LinkedHashMap<>();

    // Заполняем HashMap клюяами и пустыми массивами
    public MyFileVisit() {
        dataForMerge.put("mark01", arrayDataMark01);
        dataForMerge.put("mark17", arrayDataMark17);
        dataForMerge.put("mark23", arrayDataMark23);
        dataForMerge.put("mark35", arrayDataMark35);
        dataForMerge.put("markfx", arrayDataMarkFx);
        dataForMerge.put("markft", arrayDataMarkFt);
        dataForMerge.put("markfv", arrayDataMarkFv);
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
                    roundLineInCVSFile(path);
                    break;
                case "zip" :
                    roundFileInZIPFolder(path);
                    break;
            }
        } else {
            System.out.println("У файла " + fileName + " нет расширения");
        }
        return CONTINUE;
    }
    // Метод для парсера данных из ZIP файлв
    public void roundFileInZIPFolder(Path path) throws IOException{
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
    public void roundLineInCVSFile(Path path) throws IOException{
        // Считываем данные из файла в массив строк
        List<String> lines = Files.readAllLines(path);
        // Обходим каждую строку отдельно
        for (String s: lines) {
            parseCSVFile.parseLineFromCSV(s, dataForMerge);
        }
        System.out.println(dataForMerge);
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
        dataForMerge.forEach((k, v) ->{
            // Проверяем пустой ли массив
            if(!v.isEmpty()){
                // Сортируем данные в массиве для 3 отчета
                Collections.sort(v, Collections.reverseOrder());
                dataForReport3.put(k,v);
                // Обходим массивы и складываем значения внутри для отчета 1 и 2
                int count = 0;
                for (Integer s: v) {
                    count = count + s;
                }
                dataForReport1.put(k, count);
                dataForReport2.put(k, count);
            }else {
                // Добавляем пустые массивы для отчета 2
                dataForReport2.put(k, null);
            }
        });
        // Записываем данные в три разных файла
        System.out.println(dataForMerge);
        writeFile.jsonWriteFile(dataForReport3, "report №3.json");
        System.out.println(dataForReport3.toString());
        System.out.println("Формируем файл для отчета №1");
        writeFile.jsonWriteFile(dataForReport1, "report №1.json");
        System.out.println(dataForReport2.toString());
        System.out.println("Формируем файл для отчета №2");
        writeFile.jsonWriteFile(dataForReport2, "report №2.json");

        return TERMINATE;
    }


}
