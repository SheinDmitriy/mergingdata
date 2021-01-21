package main;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;


public class MyFileVisitTest {

    private MyFileVisit myFileVisit = new MyFileVisit();

    @Test
    public void test_parse_line_in_CSVFile() throws IOException {

        ParseCSVFile parseCSVFile = new ParseCSVFile();

        LinkedHashMap<String, ArrayList<Integer>> dataForTest = new LinkedHashMap<>();
        ArrayList<Integer> arrayDataMark01 = new ArrayList<>();
        dataForTest.put("mark01", arrayDataMark01);

        String line = "mark01,10";

        parseCSVFile.parseLineFromCSV(line, dataForTest);
        int result = dataForTest.get("mark01").get(0);

        assertEquals(result, 10);
    }
}
