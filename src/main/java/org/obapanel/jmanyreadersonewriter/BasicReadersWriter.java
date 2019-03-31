package org.obapanel.jmanyreadersonewriter;

/**
 * Simple class that stores data in a property of the class
 * Just for coding, better use a AtomicWhatever in this case
 * @param <T> data type of resource
 */
public final class BasicReadersWriter<T> extends AbsrtactReadersWriter<T> {

    private T data;

    /**
     * Constructor with no data
     */
    BasicReadersWriter(){
        this.data = null;
    }

    /**
     * Constructor with initial data
     * @param initialData data to initialize
     */
    BasicReadersWriter(T initialData){
        this.data = initialData;
    }

    @Override
    protected T executeReading() {
        return data;
    }

    @Override
    protected void executeWriting(T data) {
        this.data = data;
    }
}
