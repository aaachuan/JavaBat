package org.example.sync;

import java.util.Collections;
import java.util.Map;

public class EnhancedMap <K, V>{
    Map<K, V> map;
    public EnhancedMap(Map<K, V> map) {
        this.map = Collections.synchronizedMap(map);
    }

    public V putIfAbsent(K key, V value) {
        synchronized (map) {
            V old = map.get(key);
            if (old != null) {
                return old;
            }
            return map.put(key, value);
        }
    }


    public V get(K key) {
        return map.get(key);
    }

    public int size() {
        return map.size();
    }

}
