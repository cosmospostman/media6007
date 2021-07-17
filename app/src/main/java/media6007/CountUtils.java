package media6007;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import media6007.Parser.Document;

public class CountUtils {
    public static StringCounter countDocumentFrequency(List<Document> documents) {
        StringCounter documentCount = new StringCounter();
        for (Document d : documents) {
            for (String str : d.nGramCount.getMap().keySet()) {
                documentCount.add(str);
            }
        }
        return documentCount;
    }

    public <K, V extends Comparable<? super V>> Map<K, V> reverseSort(Map<K, V> map) {
        Map<K, V> sortedMap = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;
    }

    public static Map<String, Float> tfIdfScore(StringCounter termFrequencies, StringCounter documentFrequencies) {
        Map<String, Float> tfIdfMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> termFreq : termFrequencies.entrySet()) {
            float tfidf = termFreq.getValue() / documentFrequencies.get(termFreq.getKey());
            tfIdfMap.put(termFreq.getKey(), tfidf);
        }
        return tfIdfMap;
    }
    
}
