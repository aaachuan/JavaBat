# 异步任务执行服务

## 基本概念和原理
Java并发包异步框架提供“执行服务”的概念，将“任务的提交“和”任务的执行“相分离。
- 任务的提交：提交任务、获取结果、取消任务。
- 任务的执行：线程创建、任务调度、线程关闭等。

## 基本接口
- Runnable和Callable：表示要执行的异步任务。（Runnable不会抛出异常，Callable会。）
- Executor和ExecutorService：表示执行服务。
```
public interface Executor {

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if command is null
     */
    void execute(Runnable command);
}
```
```
public interface ExecutorService extends Executor {

    /**
     * Submits a value-returning task for execution and returns a
     * Future representing the pending results of the task. The
     * Future's {@code get} method will return the task's result upon
     * successful completion.
     *
     * <p>
     * If you would like to immediately block waiting
     * for a task, you can use constructions of the form
     * {@code result = exec.submit(aCallable).get();}
     *
     * <p>Note: The {@link Executors} class includes a set of methods
     * that can convert some other common closure-like objects,
     * for example, {@link java.security.PrivilegedAction} to
     * {@link Callable} form so they can be submitted.
     *
     * @param task the task to submit
     * @param <T> the type of the task's result
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     * @throws NullPointerException if the task is null
     */
    <T> Future<T> submit(Callable<T> task);
    
    /**
     * Submits a Runnable task for execution and returns a Future
     * representing that task. The Future's {@code get} method will
     * return the given result upon successful completion.
     *
     * @param task the task to submit
     * @param result the result to return
     * @param <T> the type of the result
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     * @throws NullPointerException if the task is null
     */
    <T> Future<T> submit(Runnable task, T result);
    
    /**
     * Submits a Runnable task for execution and returns a Future
     * representing that task. The Future's {@code get} method will
     * return {@code null} upon <em>successful</em> completion.
     *
     * @param task the task to submit
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     * @throws NullPointerException if the task is null
     */
    Future<?> submit(Runnable task);
```
- Future：表示异步任务的结果。
```
public interface Future<V> {
	boolean cancel(boolean mayInterruptIfRunning);
	boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
```
`get`方法任务最终的三种结果：
1、 正常完成，`get`方法会返回其执行结果，如果任务是`Runnable`且没有提供结果，返回`null`。

2、任务执行抛出了异常，`get`方法会将异常包装成`ExecutionException`重新抛出，通过异常的`getCause`方法可以获取原异常。

3、 任务被取消了，`get`方法会抛出异常`CancellationException`。

`如果调用get方法的线程被中断了，get方法会抛出InterruptedException`