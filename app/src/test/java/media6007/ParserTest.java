package media6007;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class ParserTest {
    @Test void testReadFiles() throws IOException {
        System.out.println("Test Read Files:");
        Parser parserUnderTest = new Parser();
        parserUnderTest.readFiles();
        System.out.println("Got " + parserUnderTest.getDocuments().size() + " documents.");
    }
}
