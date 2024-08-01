package org.example.sync;

import java.util.HashMap;
import java.util.Map;

public class EnhancedMapNewTest {
    public static void main(String[] args) throws InterruptedException {
        Map<Integer, Integer> map = new HashMap<>();
        EnhancedMap enhancedMap = new EnhancedMap(map);
        Runnable writerTask = () -> {
            for (int i = 0; i < 1000; i++) {
                enhancedMap.putIfAbsent(i, "value" + i);
            }
        };

        Runnable writerTask1 = () -> {
            for (int i = 0; i < 1000; i++) {
                enhancedMap.putIfAbsent(i, "newvalue" + i);
            }
        };


        Thread writerThread = new Thread(writerTask);
        Thread readerThread = new Thread(writerTask1);

        writerThread.start();
        readerThread.start();

        writerThread.join();
        readerThread.join();
        for (int i = 0; i < 1000; i++) {
            System.out.println(enhancedMap.get(i));
        }


    }
}
