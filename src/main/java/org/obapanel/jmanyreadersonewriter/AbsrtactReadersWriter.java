package org.obapanel.jmanyreadersonewriter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * As seen in https://codereview.stackexchange.com/questions/127234/reader-writers-problem-using-semaphores-in-java
 * And then https://en.wikipedia.org/wiki/Readers%E2%80%93writers_problem#Third_readers-writers_problem
 * @param <T> Data type of the critical resource
 */
public abstract class AbsrtactReadersWriter<T> {

    /**
     * Semaphores
     */
    private Semaphore readCountSempaphote = new Semaphore(1);
    private Semaphore resourceSemaphore = new Semaphore(1, true);
    private Semaphore serviceQueueSemaphore = new Semaphore(1,true);

    /**
     * Counter for many readers
     */
    private AtomicInteger readCount = new AtomicInteger(0);


    /**
     * Read the resource and then executes the lamnda consumer
     * The lamda is not executed into the critical section, it doesn't perform the reading execution
     * @param afterRead This consumer is executed after the reading has been done
     */
    public final void read(Consumer<T> afterRead){
        afterRead.accept(read());
    }


    /**
     * Read the resource
     * @return data from resource
     */
    public final T read() {
        T data = null;
        try {
            // Entry to read
            serviceQueueSemaphore.acquire();
            readCountSempaphote.acquire();
            if (readCount.incrementAndGet() == 1){
                resourceSemaphore.acquire();
            }
            serviceQueueSemaphore.release();
            readCountSempaphote.release();
            // Do read
            data = executeReading();
            // Exit to read
            readCountSempaphote.acquire();
            if (readCount.decrementAndGet() == 0){
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
    abstract protected T executeReading();


    /**
     * Executes the lamnda supplier and then writes the supplied data
     * The lamda is not executed into the critical section, it doesn't perform the writing execution
     * @param beforeWrite This supplier is executed before the writting process
     */
    public final void write(Supplier<T> beforeWrite){
        write(beforeWrite.get());
    }

    /**
     * Writes to the resource
     * @param data Data to be written
     */
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
    abstract protected void executeWriting(T data);


}
