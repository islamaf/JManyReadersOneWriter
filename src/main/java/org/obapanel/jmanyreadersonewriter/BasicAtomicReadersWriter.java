package org.obapanel.jmanyreadersonewriter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple class that stores data in a atomic property of the class
 * Just for coding, better use a AtomicWhatever in this case
 * @param <T> data type of resource
 */
public final class BasicAtomicReadersWriter<T>  extends  AbsrtactReadersWriter<T> {

    private AtomicReference<T> data;

    /**
     * Constructor with no data
     */
    BasicAtomicReadersWriter(){
        this.data = new AtomicReference<>();
    }

    /**
     * Constructor with initial data
     * @param initialData data to initialize
     */
    BasicAtomicReadersWriter(T initialData){
        this.data = new AtomicReference<>(initialData);
    }

    @Override
    protected T executeReading() {
        return data.get();
    }

    @Override
    protected void executeWriting(T data) {
        this.data.set(data);
    }
}
