package org.obapanel.jmanyreadersonewriter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class FunctionalReadersWriterTest {

    public FunctionalReadersWriter<Integer> functionalReadersWriter;
    public Random random = new Random();
    public Object locko = new Object();
    public final FunctionalReadersWriterTestData data = new FunctionalReadersWriterTestData();

    @Before
    public void before(){
        functionalReadersWriter = new FunctionalReadersWriter<Integer>()
                .withWriter( wdata -> {
                    if (data.isAccesing()){
                        data.thereIsAnError();
                        throw  new IllegalStateException("Should not be accessing");
                    }
                    data.setAccesing(true);
                    data.getTestList().add("WRITE");
                    accesResource(wdata);
                    data.setAccesing(false);
                }).withReader( () -> {
                    if (data.isAccesing()){
                        data.thereIsAnError();
                        throw  new IllegalStateException("Should not be accessing");
                    }
                    data.setAccesing(true);
                    data.getTestList().add("READ");
                    int rdata = accesResource(0);
                    data.setAccesing(false);
                    return rdata;
                });
    }

    @Test
    public void test_simpleCase1() throws InterruptedException {
        Thread t01 = new Thread(() -> functionalReadersWriter.write(1));
        t01.setUncaughtExceptionHandler((t, e) -> this.uncaughtException(t, e));
        t01.setName("t01");
        Thread t02 = new Thread(() -> functionalReadersWriter.read());
        t01.setUncaughtExceptionHandler((t, e) -> this.uncaughtException(t, e));
        t02.setName("t02");
        t02.start();
        t01.start();
        t01.join();
        t02.join();
        assertFalse(data.isError());
        assertTrue(data.getTestList().contains("READ"));
        assertTrue(data.getTestList().contains("WRITE"));

    }

    public void uncaughtException(Thread t, Throwable e) {
        data.thereIsAnError();
        System.out.println(e.getMessage());
        System.err.println(e.getMessage());
        e.printStackTrace();
    }
    private synchronized int accesResource(int data){
        synchronized (locko){
            try {
                long w = 10L +  random.nextInt(25)*10 + data;
                System.out.println(Thread.currentThread().getName() + " | wait " + w);
                locko.wait(w);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                return data;
            }
        }
    }

    private class FunctionalReadersWriterTestData {
        public List<String> testList = Collections.synchronizedList(new ArrayList<>());
        public boolean accesing = false;
        public boolean error = false;

        public synchronized List<String> getTestList() {
            return testList;
        }

        public synchronized boolean isAccesing() {
            return accesing;
        }

        public synchronized void setAccesing(boolean accesing) {
            this.accesing = accesing;
        }

        public boolean isError() {
            return error;
        }

        public void thereIsAnError() {
            this.error = true;
        }
    }

}
