package org.obapanel.jmanyreadersonewriter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Abstract class that lets you implement a many readers one writer solution
 * The abstract class takes care of the synchonization needed to make the reads/writes secure
 * Only one writer and no readers can be executed at a specific point
 * Many readers but no writer can be executed at a specific point
 *
 * This improves over a simple semaphore or synchronized element, as many readers can use the
 *  resource at the same time, but only one writer (and no writer and reader at the same time)
 *
 * As seen in https://codereview.stackexchange.com/questions/127234/reader-writers-problem-using-semaphores-in-java
 * And then https://en.wikipedia.org/wiki/Readers%E2%80%93writers_problem#Third_readers-writers_problem
 * @param <T> Data type of the critical resource
 */
public abstract class AbsrtactReadersWriter<T> implements IReadersWriter<T> {

    /**
     * Semaphores
     */
    private Semaphore readCountSempaphote = new Semaphore(1);
    private Semaphore resourceSemaphore = new Semaphore(1, true);
    private Semaphore serviceQueueSemaphore = new Semaphore(1,true);

    /**
     * Counter for many readers
     */
    //private AtomicInteger readCount = new AtomicInteger(0);
    private int readCount = 0;


    @Override
    public final void read(Consumer<T> afterRead){
        afterRead.accept(read());
    }


    @Override
    public final T read() {
        T data = null;
        try {
            // Entry to read
            serviceQueueSemaphore.acquire();
            readCountSempaphote.acquire();
            readCount++;
            if (readCount == 1){
                resourceSemaphore.acquire();
            }
            serviceQueueSemaphore.release();
            readCountSempaphote.release();
            // Do read
            data = executeReading();
            // Exit to read
            readCountSempaphote.acquire();
            readCount--;
            if (readCount == 0){
                resourceSemaphore.release();
            }
            readCountSempaphote.release();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    /**
     * Protected critical section that read the resource
     * Reimplement with the access to the critical section
     * Must not be synchronized (the semaphores on the class take care of synchronization)
     * @return data from resource
     */
    protected abstract T executeReading();


    @Override
    public final void write(Supplier<T> beforeWrite){
        write(beforeWrite.get());
    }

    @Override
    public final void write(T data) {
        try {
            // Entry write
            serviceQueueSemaphore.acquire();
            resourceSemaphore.acquire();
            serviceQueueSemaphore.release();
            // Do Write
            executeWriting(data);
            // Exit write
            resourceSemaphore.release();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Protected critical section that writes to the resource
     * Must not be synchronized (the semaphores on the class take care of synchronization)
     * Reimplement with the access to the critical section
     * @param data data to resource
     */
    protected abstract void executeWriting(T data);


}
