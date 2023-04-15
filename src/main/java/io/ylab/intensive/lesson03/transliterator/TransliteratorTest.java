package io.ylab.intensive.lesson03.transliterator;

public class TransliteratorTest {
    public static void main(String[] args) {
        Transliterator transliterator = new TransliteratorImpl();
        String res = transliterator
                .transliterate("HELLO! ПРИВЕТ! привет! Go, boy!");
        System.out.println(res);
    }
}

