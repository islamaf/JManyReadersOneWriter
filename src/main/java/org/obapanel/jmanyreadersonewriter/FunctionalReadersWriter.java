package org.obapanel.jmanyreadersonewriter;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @param <T> Data type of the critical resource
 */
public final class FunctionalReadersWriter<T>  extends AbsrtactReadersWriter<T> {

    private Supplier<T> reader;
    private Consumer<T> writer;

    /**
     * Constructor
     */
    public FunctionalReadersWriter(){}

    /**
     * Contructor with lambda
     * @param reader Reader lambda that will be executed into the critical section. Must return the read data
     * @param writer Writer lambda that will be executed into the critical section. Must write the given data
     */
    public FunctionalReadersWriter(Supplier<T> reader, Consumer<T> writer ){
        setReader(reader);
        setWriter(writer);
    }


    /**
     * Sets the reader lambda
     * @param reader Reader lambda that will be executed into the critical section. Must return the read data
     * @return this object
     */
    public FunctionalReadersWriter<T> withReader(Supplier<T> reader){
        setReader(reader);
        return this;
    }

    /**
     * Sets the writer lambda
     * @param writer Writer lambda that will be executed into the critical section. Must write the given data
     * @return this object
     */
    public FunctionalReadersWriter<T> withWriter(Consumer<T> writer){
        setWriter(writer);
        return this;
    }

    /**
     * Sets the reader lambda
     * @param reader Reader lambda that will be executed into the critical section. Must return the read data
     */
    void setReader(Supplier<T> reader){
        this.reader = reader;
    }

    /**
     * Sets the writer lambda
     * @param writer Writer lambda that will be executed into the critical section. Must write the given data
     */
    void setWriter(Consumer<T> writer) {
        this.writer = writer;
    }


    private void checkLamndasBeforeExecution(){
        if (reader == null) throw  new IllegalStateException("No reader defined");
        if (writer == null) throw  new IllegalStateException("No writer defined");
    }

    @Override
    protected T executeReading() {
        checkLamndasBeforeExecution();
        return reader.get();
    }

    @Override
    protected void executeWriting(T data) {
        checkLamndasBeforeExecution();
        writer.accept(data);
    }

}
