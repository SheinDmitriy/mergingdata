package main;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class MyFileVisit extends SimpleFileVisitor<Path> {

    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        System.out.println(path.getFileName().toString());
        return FileVisitResult.CONTINUE;
    }
}
