package org.obapanel.jmanyreadersonewriter.other;

import org.obapanel.jmanyreadersonewriter.IReadersWriter;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Implementation with the basic java class java.util.concurrent.locks.ReentrantReadWriteLock
 * @param <T> Data of the critical section
 */
public abstract class AbstractReadersWriterReadWriteLock<T>  implements IReadersWriter<T> {

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);



    /**
     * Read the resource and then executes the consumer
     * The lambda is NOT executed into the critical section, it doesn't perform the reading execution
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
        readWriteLock.readLock().lock();
        // Do read
        T data = executeReading();
        readWriteLock.readLock().unlock();
        return  data;
    }


    /**
     * Protected critical section that read the resource
     * Reimplement with the access to the critical section
     * Must not be synchronized (the semaphores on the class take care of synchronization)
     * @return data from resource
     */
    protected abstract T executeReading();

    /**
     * Executes the supplier and then writes the supplied data
     * The lambda is NOT executed into the critical section, it doesn't perform the writing execution
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
        readWriteLock.writeLock().lock();
        // Do Write
        executeWriting(data);
        // Exit write
        readWriteLock.writeLock().unlock();

    }

    /**
     * Protected critical section that writes to the resource
     * Must not be synchronized (the semaphores on the class take care of synchronization)
     * Reimplement with the access to the critical section
     * @param data data to resource
     */
    protected abstract void executeWriting(T data);

}
