package org.example.sync;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SynchronizedMapPutIfAbsentUnsafeExample {
    public static void main(String[] args) throws InterruptedException {
        Map<Integer, String> synchronizedMap = Collections.synchronizedMap(new HashMap<>());

        Runnable task1 = () -> putIfAbsent(synchronizedMap, 1, "Thread1");
        Runnable task2 = () -> putIfAbsent(synchronizedMap, 1, "Thread2");

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Map size: " + synchronizedMap.size());
        System.out.println("Map value: " + synchronizedMap.get(1));
    }

    private static void putIfAbsent(Map<Integer, String> map, Integer key, String value) {
        synchronized (map) {
            if (!map.containsKey(key)) {
                try {
                    // 模拟一些计算或延迟
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                map.put(key, value);
            }
        }
    }
}