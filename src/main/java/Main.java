import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    static AtomicInteger length3 = new AtomicInteger(0);
    static AtomicInteger length4 = new AtomicInteger(0);
    static AtomicInteger length5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        Thread same = new Thread(() -> {
            for (String text : texts) {
                if ((same(text))) {
                    incrementCounter(text.length());
                }
            }
            System.out.println("Красивых слов с длиной 3: " + length3.get());
        });
        same.start();

        Thread palindromes = new Thread(() -> {
            for (String text : texts) {
                if ((palindrome(text) && !same(text))) {
                    incrementCounter(text.length());
                }
            }
            System.out.println("Красивых слов с длиной 4: " + length4.get());
        });
        palindromes.start();

        Thread increases = new Thread(() -> {
            for (String text : texts) {
                if (!same(text) && increase(text)) {
                    incrementCounter(text.length());
                }
            }
            System.out.println("Красивых слов с длиной 5: " + length5.get());
        });
        increases.start();

        same.join();
        increases.join();
        palindromes.join();
    }

    static boolean increase(String text) {
        String sortedText = text.chars().mapToObj(c -> String.valueOf((char) c)).sorted().collect(Collectors.joining());
        return sortedText.equals(text);
    }

    static boolean same(String text) {
        return text.chars().distinct().count() == 1;
    }

    static boolean palindrome(String text) {
        for (int i = 0; i < text.length() / 2; i++) {
            if (text.charAt(i) != text.charAt(text.length() - i - 1)) {
                return false;
            }
        }
        return true;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void incrementCounter(int textLength) {
        if (textLength == 3) {
            length3.getAndIncrement();
        } else if (textLength == 4) {
            length4.getAndIncrement();
        } else if (textLength == 5) {
            length5.getAndIncrement();
        }
    }

}