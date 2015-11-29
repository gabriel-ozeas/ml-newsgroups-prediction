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

    private List<String> vocabulary;

    public BagOfWords(int size) {
        map = new HashMap<>();
        this.size = size;
    }

    public List<String> getVocabulary() {
        if (vocabulary == null) {
            List<Map.Entry<String, Integer>> list = new LinkedList(map.entrySet());
            sort(list, (o1, o2) -> ((Comparable) o2.getValue()).compareTo(o1.getValue()));

            vocabulary = list
                    .stream()
                    .skip(300)
                    .limit(size)
                    .map(item -> item.getKey()).collect(toList());
        }

        return vocabulary;
    }

    public void count(String word) {
        map.computeIfAbsent(word, w -> 1);
        map.computeIfPresent(word, (w, number) -> number + 1);
    }

    public int sizeOfAll() {
        return map.size();
    }

    public int limit() {
        return size;
    }
}
