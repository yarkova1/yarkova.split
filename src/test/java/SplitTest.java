import org.junit.Test;
import org.yarkova.split.Main;
import org.yarkova.split.Split;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SplitTest {

    private void createAndFillFile(String filename, String content) throws IOException {
        new FileWriter(filename).append(content).close();
    }

    private void assertFileContent(String actualFilename, String expectedContent) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (FileReader fr = new FileReader(actualFilename)) {
            int ch;
            while ((ch = fr.read()) != -1) {
                resultStringBuilder.append((char) ch);
            }
        }
        assertEquals(expectedContent, resultStringBuilder.toString());
    }

    private void deleteFile(String filename) {
        new File(filename).delete();
    }

    @Test
    public void testExtension() {
        assertEquals("", Split.extensionIfExist("hello"));
        assertEquals(".ru", Split.extensionIfExist("hello.ru"));
        assertEquals("", Split.extensionIfExist("helloru"));
        assertEquals(".txt", Split.extensionIfExist("helloru.txt"));
    }

    @Test
    public void testNameWithoutExtension() {
        assertEquals("hello", Main.getNameWithoutExtension("hello"));
        assertEquals("hello", Main.getNameWithoutExtension("hello.ru"));
        assertEquals("helloru", Main.getNameWithoutExtension("helloru"));
        assertEquals("helloru", Main.getNameWithoutExtension("helloru.txt"));
    }

    @Test
    public void testSplitsByLines() throws IOException {
        createAndFillFile("input", "hello" + System.lineSeparator() + "my" + System.lineSeparator() + "world");
        Main.main(Arrays.stream("input -l 1".split(" ")).toArray(String[]::new));

        assertFileContent("xaa", "hello" + System.lineSeparator());
        assertFileContent("xab", "my" + System.lineSeparator());
        assertFileContent("xac", "world" + System.lineSeparator());
        deleteFile("xaa");
        deleteFile("xab");
        deleteFile("xac");


        Main.main(Arrays.stream("input -d -l 1".split(" ")).toArray(String[]::new));

        assertFileContent("x1", "hello" + System.lineSeparator());
        assertFileContent("x2", "my" + System.lineSeparator());
        assertFileContent("x3", "world" + System.lineSeparator());
        deleteFile("x1");
        deleteFile("x2");
        deleteFile("x3");


        createAndFillFile("input","Hi");
        Main.main(Arrays.stream("input -l 1".split(" ")).toArray(String[]::new));

        assertFileContent("xaa", "Hi" + System.lineSeparator());
        deleteFile("xaa");


        Main.main(Arrays.stream("input -o - -l 1".split(" ")).toArray(String[]::new));

        assertFileContent("inputaa", "Hi" + System.lineSeparator());
        deleteFile("inputaa");


        Main.main(Arrays.stream("input -o output -l 1".split(" ")).toArray(String[]::new));

        assertFileContent("outputaa", "Hi" + System.lineSeparator());
        deleteFile("outputaa");

        Main.main(Arrays.stream("input -o output -d -l 1".split(" ")).toArray(String[]::new));

        assertFileContent("output1", "Hi" + System.lineSeparator());
        deleteFile("output1");

        deleteFile("input");
    }


    @Test
    public void testSplitsByChars() throws IOException {
        createAndFillFile("input", "hello world");
        Main.main(Arrays.stream("-c 3 input".split(" ")).toArray(String[]::new));

        assertFileContent("xaa", "hel");
        assertFileContent("xab", "lo ");
        assertFileContent("xac", "wor");
        assertFileContent("xad", "ld");
        deleteFile("xaa");
        deleteFile("xab");
        deleteFile("xac");
        deleteFile("xad");


        Main.main(Arrays.stream("input -c 6 -d".split(" ")).toArray(String[]::new));

        assertFileContent("x1", "hello ");
        assertFileContent("x2", "world");
        deleteFile("x1");
        deleteFile("x2");


        Main.main(Arrays.stream("input -c 7 -d -o out".split(" ")).toArray(String[]::new));

        assertFileContent("out1", "hello w");
        assertFileContent("out2", "orld");
        deleteFile("out1");
        deleteFile("out2");


        Main.main(Arrays.stream("input -c 11 -d".split(" ")).toArray(String[]::new));

        assertFileContent("x1", "hello world");
        deleteFile("x1");

        deleteFile("input");
    }

    @Test
    public void testSplitsByFiles() throws IOException {
    createAndFillFile("input", "hello world");

        Main.main(Arrays.stream("input -n 1 -d -o out".split(" ")).toArray(String[]::new));

        assertFileContent("out1", "hello world");
        deleteFile("out1");


        Main.main(Arrays.stream("input -n 2 -d -o -".split(" ")).toArray(String[]::new));

        assertFileContent("input1", "hello ");
        assertFileContent("input2", "world");
        deleteFile("input1");
        deleteFile("input2");


        Main.main(Arrays.stream("input -n 3 -d".split(" ")).toArray(String[]::new));

        assertFileContent("x1", "hell");
        assertFileContent("x2", "o wo");
        assertFileContent("x3", "rld");
        deleteFile("x1");
        deleteFile("x2");
        deleteFile("x3");


        deleteFile("input");
    }

    @Test
    public void testCharsInFile() throws IOException {
        String text = "Just random text 12ijsjs";

        createAndFillFile("input", text);
        assertEquals(text.length(), Split.charsInFile("input"));
        deleteFile("input");

        createAndFillFile("input", text.repeat(2));
        assertEquals(text.length() * 2, Split.charsInFile("input"));
        deleteFile("input");

        createAndFillFile("input", text.repeat(5));
        assertEquals(text.length() * 5, Split.charsInFile("input"));
        deleteFile("input");

    }
}
