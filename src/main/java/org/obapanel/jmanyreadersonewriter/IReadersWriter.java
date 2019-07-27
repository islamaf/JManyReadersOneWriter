package org.obapanel.jmanyreadersonewriter;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IReadersWriter<T> {
    /**
     * Read the resource and then executes the consumer
     * The lambda is NOT executed into the critical section, it doesn't perform the reading execution
     * @param afterRead This consumer is executed after the reading has been done
     */
    void read(Consumer<T> afterRead);

    /**
     * Read the resource
     * @return data from resource
     */
    T read();

    /**
     * Executes the supplier and then writes the supplied data
     * The lambda is NOT executed into the critical section, it doesn't perform the writing execution
     * @param beforeWrite This supplier is executed before the writting process
     */
    void write(Supplier<T> beforeWrite);

    /**
     * Writes to the resource
     * @param data Data to be written
     */
    void write(T data);

    /*
    T executeReading();

     void executeWriting(T data);
     */


}
