package org.example.basic;

public class PrintRoundThread {

    private static volatile String s = "B";
    public static void main(String[] args) {
        new Thread(()->{
            for(;;) {
            synchronized (s) {
                    if (s == "B") {
                        System.out.print("A");
                        s = "A";
                    }
                }
            }
        }).start();

        new Thread(()->{
            for(;;) {
            synchronized (s) {

                    if (s == "A") {
                        System.out.print("B");
                        s = "B";
                    }
                }
            }
        }).start();

    }


}
