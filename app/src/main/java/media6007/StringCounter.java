package media6007;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class StringCounter {
    private Map<String, Integer> countMap = new LinkedHashMap<>();

    public void add(String str) {
        add(str, 1);
    }

    public void add(String str, int count) {
        if (countMap.containsKey(str)) {
            countMap.put(str, countMap.get(str) + count);
        } else {
            countMap.put(str, count);
        }
    }

    public void addAll(StringCounter sc) {
        for (Map.Entry<String, Integer> e :  sc.entrySet()) {
            add(e.getKey(), e.getValue());
        }
    }

    public Set<Entry<String, Integer>> entrySet() {
        return countMap.entrySet();
    }

    public int get(String str) {
        return countMap.get(str);
    }

    public Map<String, Integer> getMap() {
        return countMap;
    }

    public boolean containsKey(String key) {
        return countMap.containsKey(key);
    }
}
