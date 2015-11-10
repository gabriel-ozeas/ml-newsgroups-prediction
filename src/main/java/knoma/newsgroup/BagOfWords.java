package knoma.newsgroup;

import java.util.*;

import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;

/**
 * Created by gabriel on 01/11/15.
 */
public class BagOfWords {
    private Map<String,Integer> map;
    private int size;

    public BagOfWords(int size) {
        map = new HashMap<>();
        this.size = size;
    }

    public List<String> getVocabulary() {
        List<Map.Entry<String, Integer>> list = new LinkedList(map.entrySet());
        sort(list, (o1, o2) -> ((Comparable) o2.getValue()).compareTo(o1.getValue()));

        return list.stream().limit(size).map(item -> item.getKey()).collect(toList());
    }

    public void count(String word) {
        map.computeIfAbsent(word, w -> 1);
        map.computeIfPresent(word, (w, number) -> number + 1);
    }

}
