package media6007;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import com.google.common.collect.Iterators;
import com.google.common.io.Files;

public class Parser {
    
    private final static String DOCUMENT_DELIMITER = "____________________________________________________________";

    public static class Document {
        String fullText;
        String credit;
        String title;
        String date;
        String section;
        String hashId;
        transient StringCounter nGramCount;

        //TODO: bag of keywords, less exclusions
        //TODO: hashID

        protected void countNGrams() {
            nGramCount = countNGramsFromText(fullText);
        }

        protected void setHashId() {
            String date = "XX";
            String titleWord = "XX";
            String textWord = "XX";
            if (this.date != null) {date = this.date;}
            if (this.title != null) {titleWord = this.title.split("\\s+")[0];}
            if (this.fullText != null) {textWord = fullText.split("\\s+")[0];}
            this.hashId = this.date + "_" + titleWord + textWord;
            this.hashId = this.hashId.replaceAll("[^a-zA-Z0-9\\s]", "");
            this.hashId = this.hashId.replaceAll("\\s+","");
        }

        protected static StringCounter countNGramsFromText(String text) {
            StringCounter nGramCount = new StringCounter();

            Iterator<String> words = Iterators.forArray(text.split("\\s+"));
            String previousWord = null;
            String thisWord;
            while (words.hasNext()) {
                thisWord = words.next();
                thisWord = thisWord.toLowerCase();
                thisWord = thisWord.replaceAll("[^a-zA-Z0-9\\s]", ""); // alphanumeric chars only
                if (thisWord.isEmpty()) {
                    continue;
                }
                nGramCount.add(thisWord);
                if(previousWord != null) {
                    nGramCount.add(previousWord + " " + thisWord);
                }
                previousWord = thisWord;
            }

            // Reverse sort
            return nGramCount;
        }
    }

    private List<Document> documents = new LinkedList<>();
    private Document currentDocument;
    private boolean fullTextMode = false;
    private StringJoiner fullTextJoiner;

    public void readFiles(String dataPath) throws IOException {
        File[] files = new File(dataPath).listFiles();
        // System.out.println("Reading files:");
        for (File f : files) {
            if (f.isFile()) {
                if(Files.getFileExtension(f.getAbsolutePath()).equals("txt")) {
                    // System.out.println(f.getName());
                    List<String> lines = Files.asCharSource(f, StandardCharsets.UTF_8).readLines();
                    lines.forEach(this::processLine);
                }
            }
        }
    }

    protected void processLine(String line) {
        if (line.equals(DOCUMENT_DELIMITER)) {
            if (currentDocument != null) {
                currentDocument.countNGrams();
                currentDocument.setHashId();
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
            if (line.startsWith("CREDIT: By ")) {
                currentDocument.credit = line.replace("CREDIT: By ", "");
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