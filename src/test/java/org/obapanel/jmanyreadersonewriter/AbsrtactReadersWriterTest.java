package org.obapanel.jmanyreadersonewriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbsrtactReadersWriterTest {



    TestAbsrtactReadersWriter testAbsrtactReadersWriter;

    void x(){
        testAbsrtactReadersWriter = new TestAbsrtactReadersWriter();
    }


    private class TestAbsrtactReadersWriter extends AbsrtactReadersWriter<Integer> {

        public List<String> testList = Collections.synchronizedList(new ArrayList<>());

        public int data = 0;

        @Override
        protected Integer executeReading() {
            testList.add("READ");
            return data;
        }

        @Override
        protected void executeWriting(Integer data) {
            testList.add("WRITE");
            this.data = data;
        }
    }
}
