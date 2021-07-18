package media6007;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import media6007.Parser.Document;

public class AnalysisMain {
    public static final String DATA_PATH = "/Users/mlj/Development/media6007/annotator/html/data/annotations.json";

    public static Map<String, List<String>> readFiles(String dataPath) throws IOException {
        Type type = new TypeToken<Map<String, List<String>>>(){}.getType();

        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(DATA_PATH));
        Map<String, List<String>> annotations = gson.fromJson(reader, type);
        return annotations;
    }

    public static void printTagCount(Map<String, List<String>> annotations) {  
        StringCounter sc = new StringCounter();      
        for (List<String> tags : annotations.values()) {
            for (String tag : tags) {
                sc.add(tag);
            }
        }
        sc.printAll();
    }

    public static void printSectionCountNotExcluded(List<Document> documents, Map<String, List<String>> annotations) {
        StringCounter sc = new StringCounter();
        for (Document d : documents) {
            List<String> tags = annotations.get(d.hashId);
            if (tags != null && tags.contains("EXCLUDE")) {
                sc.add("excluded");
            } else {
                sc.add(d.section);
            }
        }
        sc.printAll();
    }

    public static void printTagCountWithFilter(List<Document> documents, Map<String, List<String>> annotations) {
        StringCounter sc = new StringCounter();
        for (Document d : documents) {
            if (d.section != null && ! d.section.equals("Letters")) {
                List<String> tags = annotations.get(d.hashId);
                if (tags != null && tags.contains("EXCLUDE")) {
                    // sc.add("excluded");
                } else if (tags != null) {
                    for (String tag : tags) {
                        sc.add(tag);
                    }
                }
            }
            
        }
        sc.printAll();
    }

    public static void main(String[] args) throws IOException {
        List<Document> selectedDocuments = Main.selectDocuments();
        Map<String, List<String>> annotations = readFiles(DATA_PATH);
        //printTagCount(annotations);
        // printSectionCountNotExcluded(selectedDocuments, annotations);
        printTagCountWithFilter(selectedDocuments, annotations);
    }
}
