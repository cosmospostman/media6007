package media6007;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import com.google.common.io.Files;

public class Parser {
    
    final static String DATA_PATH = "/Users/mlj/Development/media6007/data/";

    final static private String DOCUMENT_DELIMITER = "____________________________________________________________";

    public static class Document {
        String fullText;
        String credit;
        String title;
        String date;
        String section;
        //TODO: bag of keywords, less exclusions
        //TODO: hashID

    }

    private List<Document> documents = new LinkedList<>();
    private Document currentDocument;
    private boolean fullTextMode = false;
    private StringJoiner fullTextJoiner;

    public void readFiles() throws IOException {
        File[] files = new File(DATA_PATH).listFiles();
        System.out.println("Reading files:");
        for (File f : files) {
            if (f.isFile()) {
                if(Files.getFileExtension(f.getAbsolutePath()).equals("txt")) {
                    System.out.println(f.getName());
                    List<String> lines = Files.asCharSource(f, StandardCharsets.UTF_8).readLines();
                    lines.forEach(this::processLine);
                }
            }
        }

    }

    protected void processLine(String line) {
        if (line.equals(DOCUMENT_DELIMITER)) {
            if (currentDocument != null) {
                documents.add(currentDocument);
            }
            currentDocument = new Document();
            fullTextJoiner = new StringJoiner("\n");
            return;
        }

        if (line.startsWith("Full text: ")) {
            line = line.replace("Full text: ", "");
            fullTextMode = true;
            // Continue
        }

        if (line.isEmpty()) {
            fullTextMode = false;
            currentDocument.fullText = fullTextJoiner.toString();
            return;
        }

        if (fullTextMode) {
            if (line.startsWith("CREDIT: ")) {
                currentDocument.credit = line.replace("CREDIT: ", "");
            } else {
                fullTextJoiner.add(line);
            }
            return;
        }

        if (line.startsWith("Title: ")) {
            currentDocument.title = line.replace("Title: ", "");
            return;
        }

        if (line.startsWith("Publication date: ")) {
            currentDocument.date = line.replace("Publication date: ", "");
            return;
        }

        if (line.startsWith("Section: ")) {
            currentDocument.section = line.replace("Section: ", "");
            return;
        }


    }

    public List<Document> getDocuments() {
        return documents;
    }

}