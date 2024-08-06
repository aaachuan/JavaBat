package org.example.exec;

import java.util.Random;
import java.util.concurrent.*;

public class BasicUsing implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sleepSeconds = new Random().nextInt(1000);
        Thread.sleep(sleepSeconds);
        return sleepSeconds;
    }

    public static void main(String[] args) throws InterruptedException {
        //ExecutorService executor = Executors.newSingleThreadExecutor();
        ExecutorService executor = new SimpleExecutorService();
                Future<Integer> future = executor.submit(new BasicUsing());
        Thread.sleep(100);
        try {
            System.out.println(future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
