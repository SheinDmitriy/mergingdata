package main;


import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;


public class MyFileVisitTest {

    private MyFileVisit myFileVisit = new MyFileVisit();

    @Test
    public void test_parse() throws IOException {

        Path path = Paths.get("src/test/resources/test.csv");
        Integer i = 24;

        try {
            myFileVisit.roundLineInCVSFile(path);
            assertEquals(myFileVisit.getDataForMerge().get("mark17").get(0), i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
