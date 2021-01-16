package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Задаем путь к файлам и запускаем метод осклейки данных
        Path path = Paths.get("merge_file");
        runMerge(path);
    }

    // Метод для склейки данных
    private static void runMerge(Path path){
        try {
            // Используем метод walkFileTree из java nio для обхода всех файлов в заданной дериктории
            Files.walkFileTree(path, new MyFileVisit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
