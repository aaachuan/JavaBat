package org.example.sync;

import java.util.HashMap;
import java.util.Map;

public class EnhancedMapTest {
    public static void main(String[] args) throws InterruptedException {
        Map map = new HashMap();
        map.put("A", "B");
        EnhancedMap enhancedMap = new EnhancedMap(map);
        new Thread(()-> {
            while (enhancedMap.get("A") != null) {

            }
            System.out.println("exit loop");
        }).start();

        Thread.sleep(1000);
        //enhancedMap.put("A", null);
        System.out.println("exit main");
    }
}
