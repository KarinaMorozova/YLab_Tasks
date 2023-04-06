package io.ylab.intensive.lesson03.datedmap;

public class DateMapTest {
    public static void main(String[] args) throws InterruptedException {
        DatedMap datedMap = new DatedMapImpl();

        datedMap.put("1", "один");
        datedMap.put("2", "два");
        datedMap.put("3", "три");
        datedMap.put("4", "четыре");
        datedMap.put("5", "пять");

        System.out.println(datedMap.get("2"));
        System.out.println(datedMap.getKeyLastInsertionDate("2"));

        Thread.sleep(2000);

        datedMap.put("2", "это шесть");
        System.out.println(datedMap.get("2"));
        System.out.println(datedMap.getKeyLastInsertionDate("2"));


    }
}
