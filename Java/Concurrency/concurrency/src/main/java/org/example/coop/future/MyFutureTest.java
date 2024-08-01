package org.example.coop.future;

import java.util.concurrent.Callable;

public class MyFutureTest {
    public static void main(String[] args) {
        MyExecutor executor = new MyExecutor();

        Callable<Integer> subTask = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int mills = (int) (Math.random() * 1000);
                Thread.sleep(mills);
                return mills;
            }
        };

        MyFuture<Integer> future = executor.execute(subTask);
        try {
            Integer result = future.get();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
