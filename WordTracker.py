class WordTracker:
    def __init__(self, sentence):
        self.sentence = sentence
        self.word_counts = {}

    def count_words(self):
        words = self.sentence.split(" ")
        for word in words:
            if word in self.word_counts:
                self.word_counts[word] += 1
            else:
                self.word_counts[word] = 1

    def get_word_frequency(self, word):
        return self.word_counts.get(word, 0)

    def print_frequencies(self):
        for word, count in self.word_counts.items():
            print(f"{word}: {count}")

tracker = WordTracker("hello world hello again world")
tracker.count_words()
tracker.print_frequencies()
print("Frequency of 'hello':", tracker.get_word_frequency("hello"))
