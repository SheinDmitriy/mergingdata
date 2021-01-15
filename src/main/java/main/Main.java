package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        Path path = Paths.get("merge_file");
        runMerge(path);
    }

    private static void runMerge(Path path){

        try {
            Files.walkFileTree(path, new MyFileVisit());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
