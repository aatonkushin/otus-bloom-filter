package org.example;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 */
public class App {
    private static final int max = 1_000_000;
    
    public static void main(String[] args) throws IOException {
        System.out.println("Google Guava Bloom Filter Test");
        integersTest(1);
        integersTest(2);
        integersTest(3);

        stringTest(1);
        stringTest(2);
        stringTest(30);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void integersTest(int divider){
        BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(), max / divider, 0.01);

        System.out.println("Test with integers");
        System.out.println("Fill with even integers from 0 to " + max);
        for (int i = 0; i < max; i += 2) {
            filter.put(i);
        }

        System.out.println("Check false positives with odd integers...");
        int count = 0;
        for (int i = 1; i < max; i += 2) {
            if (filter.mightContain(i)) {
                count++;
            }
        }

        System.out.printf("False positive count: %d (%f percent)%n", count, 100.0 * count / max);
        System.out.println();
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void stringTest(int divider) throws IOException {
        System.out.println("Test with Strings");
        Path path = Paths.get("./Капитанская_дочка.txt");
        String text = String.join("", Files.readAllLines(path));
        String[] words = text.split("[\\s,.;!]");
        BloomFilter<String> filter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), words.length / divider, 0.01);

        for (String word : words) {
            filter.put(word);
        }

        System.out.println("Might contains Отец: "+filter.mightContain("Отец"));
        System.out.println("Might contains Мать: "+filter.mightContain("Мать"));
        System.out.println("Might contains энергия: "+filter.mightContain("энергия"));
        System.out.println("Might contains молодость: "+filter.mightContain("молодость"));

        System.out.println();
    }
}
