package org.obapanel.jmanyreadersonewriter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class AbsrtactReadersWriterTest {



    TestAbsrtactReadersWriter testAbsrtactReadersWriter;

    @Test
    public void test_simpleCase1() throws InterruptedException {
        for(int i=0; i < 10; i++) {
            final TestAbsrtactReadersWriter testAbsrtactReadersWriter = new TestAbsrtactReadersWriter();
            Thread t01 = new Thread(() -> testAbsrtactReadersWriter.write((Void)null));
            t01.setUncaughtExceptionHandler( (t,e)->  this.uncaughtException(testAbsrtactReadersWriter, t,e));
            t01.setName("t01");
            Thread t02 = new Thread(() -> testAbsrtactReadersWriter.read());
            t01.setUncaughtExceptionHandler( (t,e)->  this.uncaughtException(testAbsrtactReadersWriter, t,e));
            t02.setName("t02");
            t01.start();
            t02.start();
            t01.join();
            t02.join();
            assertFalse(testAbsrtactReadersWriter.error);
            assertTrue(testAbsrtactReadersWriter.testList.contains("READ"));
            assertTrue(testAbsrtactReadersWriter.testList.contains("WRITE"));
        }
    }

    @Test
    public void test_simpleCase2() throws InterruptedException {
        for(int i=0; i < 10; i++) {
            final TestAbsrtactReadersWriter testAbsrtactReadersWriter = new TestAbsrtactReadersWriter();
            Thread t01 = new Thread(() -> testAbsrtactReadersWriter.write((Void) null));
            t01.setUncaughtExceptionHandler((t, e) -> this.uncaughtException(testAbsrtactReadersWriter, t, e));
            t01.setName("t01");
            Thread t02 = new Thread(() -> testAbsrtactReadersWriter.read());
            t01.setUncaughtExceptionHandler((t, e) -> this.uncaughtException(testAbsrtactReadersWriter, t, e));
            t02.setName("t02");
            t02.start();
            t01.start();
            t01.join();
            t02.join();
            assertFalse(testAbsrtactReadersWriter.error);
            assertTrue(testAbsrtactReadersWriter.testList.contains("READ"));
            assertTrue(testAbsrtactReadersWriter.testList.contains("WRITE"));
        }
    }

    public void uncaughtException(TestAbsrtactReadersWriter  testAbsrtactReadersWriter, Thread t, Throwable e) {
        testAbsrtactReadersWriter.error = true;
        System.out.println(e.getMessage());
        System.err.println(e.getMessage());
        e.printStackTrace();
    }


    private class TestAbsrtactReadersWriter extends AbsrtactReadersWriter<Void> {

        public List<String> testList = Collections.synchronizedList(new ArrayList<>());
        public boolean accesing = false;
        public boolean error = false;
        public Random random = new Random();
        public Object locko = new Object();

        @Override
        public Void executeReading() {
            if (accesing){
                error = true;
                throw  new IllegalStateException("Should not be accessing");
            }
            accesing = true;
            testList.add("READ");
            accesResource();
            accesing = false;
            return null;
        }

        @Override
        public void executeWriting(Void data) {
            if (accesing){
                error = true;
                throw  new IllegalStateException("Should not be accessing");
            }
            accesing = true;
            testList.add("WRITE");
            accesResource();
            accesing = false;
        }

        private synchronized void accesResource(){
            synchronized (locko){
                try {
                    long w = 10L +  random.nextInt(25)*10L;
                    System.out.println(Thread.currentThread().getName() + " | wait " + w);
                    locko.wait(w);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
