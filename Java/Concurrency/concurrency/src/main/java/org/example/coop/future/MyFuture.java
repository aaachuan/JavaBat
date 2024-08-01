package org.example.coop.future;

public interface MyFuture <V>{
    V get() throws Exception;
}
