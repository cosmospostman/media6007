package media6007;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static List<Document> uniq(List<Document> documents) {
        List<Document> uniqDocuments = new LinkedList<>();
        Set<String> hashIdSeen = new HashSet<String>();
        for (Document d : documents) {
            if (! hashIdSeen.contains(d.hashId)) {
                hashIdSeen.add(d.hashId);
                uniqDocuments.add(d);
            }
        }
        return uniqDocuments;
    }

    public static StringCounter countTermFrequency(List<Document> documents) {
        StringCounter termCount = new StringCounter();
        for (Document d : documents) {
            termCount.addAll(d.nGramCount);
        }
        return termCount;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> reverseSort(Map<K, V> map) {
        Map<K, V> sortedMap = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;
    }

    public static Map<String, Float> tfIdfScore(StringCounter termFrequencies, StringCounter documentFrequencies, StringCounter globalTermFrequencies) {
        Map<String, Float> tfIdfMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> termFreq : termFrequencies.entrySet()) {
            float tfidf = (float) termFreq.getValue() * (float) documentFrequencies.get(termFreq.getKey());
            tfidf = tfidf / (float) globalTermFrequencies.get(termFreq.getKey());
            tfIdfMap.put(termFreq.getKey(), tfidf);
        }
        return tfIdfMap;
    }
    
}
