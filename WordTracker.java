import java.util.HashMap;
import java.util.Map;

public class WordTracker {
    public String sentence;
    public Map<String, Integer> wordCounts;

    public WordTracker(String sentence) {
        this.sentence = sentence;
        this.wordCounts = new HashMap<>();
    }

    public void countWords() {
        String[] words = sentence.split(" ");
        for (String word : words) {
            if (wordCounts.containsKey(word)) {
                wordCounts.put(word, wordCounts.get(word) + 1);
            } else {
                wordCounts.put(word, 1);
            }
        }
    }

    public int getWordFrequency(String word) {
        return wordCounts.getOrDefault(word, 0);
    }

    public void printFrequencies() {
        for (String word : wordCounts.keySet()) {
            System.out.println(word + ": " + wordCounts.get(word));
        }
    }

    public static void main(String[] args) {
        WordTracker tracker = new WordTracker("hello world hello again world");
        tracker.countWords();
        tracker.printFrequencies();
        System.out.println("Frequency of 'hello': " + tracker.getWordFrequency("hello"));
    }
}
