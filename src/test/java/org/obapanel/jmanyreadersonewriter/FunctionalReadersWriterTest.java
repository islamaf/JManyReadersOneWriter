package org.obapanel.jmanyreadersonewriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionalReadersWriterTest {

    public List<String> testList = Collections.synchronizedList(new ArrayList<>());
    public int data;
    public FunctionalReadersWriter<Integer> functionalReadersWriter;

    void x(){
        data = 0;
        testList = Collections.synchronizedList(new ArrayList<>());
        functionalReadersWriter = new FunctionalReadersWriter<Integer>()
                .withWriter( wdata -> {
                    testList.add("WRITE");
                    data = wdata;
                }).withReader( () -> {
                    testList.add("READ");
                    return data;
                });
    }

}
