package main;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class MyFileVisit extends SimpleFileVisitor<Path> {

    private static Map<String, Integer> merge_key= new HashMap<>();

    static {
        merge_key.put("mark01", 0);
        merge_key.put("mark17", 0);
        merge_key.put("mark23", 0);
        merge_key.put("mark35", 0);
        merge_key.put("markFV", 0);
        merge_key.put("markFX", 0);
        merge_key.put("markFT", 0);
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

        return FileVisitResult.CONTINUE;
    }

    private void parseZIP(Path path) {
    }

    private void parseCSV(Path path) {

    }
}
