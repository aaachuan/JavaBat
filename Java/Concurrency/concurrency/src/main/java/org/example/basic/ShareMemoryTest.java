package org.example.basic;

import java.util.ArrayList;
import java.util.List;

public class ShareMemoryTest {
    private static int share = 0;
    private static void incrShare(){
        share++;
    }

    static class SubThread implements Runnable {
        List<String> list;

        SubThread(List<String> list) {
            this.list = list;
        }

        @Override
        public void run() {
            incrShare();
            list.add(Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<>();
        Thread t1 = new Thread(new SubThread(list));
        Thread t2 = new Thread(new SubThread(list));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(share);
        System.out.println(list);

    }
}
