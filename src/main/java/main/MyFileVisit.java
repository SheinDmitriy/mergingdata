package main;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyFileVisit extends SimpleFileVisitor<Path> {

    private static FileWriter jsonfile;

    private JSONObject jsonObjectV1 = new JSONObject();
    private JSONObject jsonObjectV2 = new JSONObject();
    private JSONObject jsonObjectV3 = new JSONObject();

    private static Map<String, Integer> merge_key = new HashMap<>();
    static {
        merge_key.put("mark01", null);
        merge_key.put("mark17", null);
        merge_key.put("mark23", null);
        merge_key.put("mark35", null);
        merge_key.put("markfv", null);
        merge_key.put("markfx", null);
        merge_key.put("markft", null);
    }

    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        String fileName = path.getFileName().toString();
        System.out.println("Обрабатываем файл: " + fileName);

        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
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

    private void parseZIP(Path path) throws IOException{
        FileSystem fs = FileSystems.newFileSystem(path, null);
        Path zipPath = fs.getPath("/");

        try {
            Files.walkFileTree(zipPath, new MyFileVisit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseCSV(Path path) throws IOException{
        List<String> lines = Files.readAllLines(path);
        for (String s: lines) {
            int count = 0;
            if(!s.startsWith("#")){
                String[] date = s.split(",");
                String key = date[0].toLowerCase();
                if( merge_key.get(key) != null) {
                    count = merge_key.get(key);
                }
                count = count + Integer.parseInt(date[1]);
                merge_key.put(key,count);
            }
        }
    }

    @Override
    public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
        if(path.toString().equals("/")){
            return TERMINATE;
        }
        System.out.println("Формируем данные для отчета №1");
        merge_key.forEach((k, v) ->{
            if(v != null){
                jsonObjectV1.put(k,v);
            }
        });
        System.out.println(jsonObjectV1.toString());
        System.out.println("Формируем файл для отчета №1");
        jsonWriteFile(jsonObjectV1, "report №1.txt");

        System.out.println("Формируем данные для отчета №2");
        jsonObjectV2.putAll(merge_key);
        System.out.println(jsonObjectV2.toString());
        System.out.println("Формируем файл для отчета №2");
        jsonWriteFile(jsonObjectV2, "report №2.txt");

        System.out.println("Формируем данные для отчета №3");

        return TERMINATE;
    }

    private void jsonWriteFile(JSONObject jsonObject, String filename) throws IOException{
        jsonfile = new FileWriter("report/" + filename);
        jsonfile.write(jsonObject.toJSONString());
        System.out.println("Файл " + filename + " успешно создан");
        jsonfile.flush();
        jsonfile.close();
    }
}
