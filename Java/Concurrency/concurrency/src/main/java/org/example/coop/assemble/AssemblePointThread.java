package org.example.coop.assemble;

public class AssemblePointThread extends Thread{
    AssemblePoint ap;
    public AssemblePointThread(AssemblePoint ap) {
        this.ap = ap;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((int)(Math.random()*1000));
            ap.await();
            System.out.println("arrived");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        int num = 10;
        AssemblePoint point = new AssemblePoint(num);
        for (int i = 0; i < num; i++) {
            AssemblePointThread thread = new AssemblePointThread(point);
            thread.start();
        }
    }
}
