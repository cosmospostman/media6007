package media6007;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import media6007.Parser.Document;

class ParserTest {

    private final static String TEST_DATA_PATH = "src/test/resources/";

    private String getAbsoluteDataPath(String path) {
        return new File(path).getAbsolutePath();
    }

    @Test void testReadFiles() throws IOException {
        System.out.println("Test Read Files:");
        Parser parserUnderTest = new Parser();
        parserUnderTest.readFiles(getAbsoluteDataPath(TEST_DATA_PATH));

        List<Document> results = parserUnderTest.getDocuments();
        assertEquals(2, results.size(),
            "Didn't get expected number of documents.");
        assertEquals("PETER HEMPHILL", results.get(0).credit,
            "Unexpected credit field");
        assertEquals("Jul 1, 2020", results.get(0).date,
            "Unexpected date field");
        assertEquals("News", results.get(0).section,
            "Unexpected section field");
        assertEquals("Fraud cow case delayed", results.get(0).title,
            "Unexpected title field");
        assertTrue(results.get(0).fullText.startsWith("THE fraud case brought by NSW Police"),
            "Unexpected start to fullText field");
    }

    @Test void testGetNGramsFromText() {
        String text = "text; - with (climate change) climate change bent";
        Map<String, Integer> expected = Map.of(
            "with", Integer.valueOf(1),
            "with climate", Integer.valueOf(1),
            "change climate", Integer.valueOf(1),
            "change", Integer.valueOf(2),
            "bent", Integer.valueOf(1),
            "text with", Integer.valueOf(1),
            "text", Integer.valueOf(1),
            "climate", Integer.valueOf(2),
            "change bent", Integer.valueOf(1),
            "climate change", Integer.valueOf(2)
            );
        Map<String, Integer> actual = Parser.Document.countNGramsFromText(text);
        assertEquals(expected, actual);
    }
}
