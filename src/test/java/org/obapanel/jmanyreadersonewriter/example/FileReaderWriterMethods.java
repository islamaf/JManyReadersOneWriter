package org.obapanel.jmanyreadersonewriter.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class FileReaderWriterMethods {

    private static Random random = new Random();
    private static Object locko = new Object();

    private static final boolean REALLY_DO_WAITING = false;
    private static void waiting(){
        if (REALLY_DO_WAITING){
            doWaiting();
        }
    }

    private synchronized static void doWaiting(){
        synchronized (locko){
            try {
                //long w = 10L;
                long w = 10L +  random.nextInt(25)*100L;
                System.out.println(Thread.currentThread().getName() + " | wait " + w);
                locko.wait(w);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static String executeReading(String fileName) {
        return executeReading(new File(fileName));
    }


    public static String executeReading(File file) {
        System.out.println("> READ  IN  " + Thread.currentThread().getName());
        waiting();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line != null && line.length() > 0 && line.trim().length() > 0 ){
                    sb.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("> READ OUT " + Thread.currentThread().getName());
        return sb.toString();
    }

    public static void executeWriting(String fileName, String data) {
        executeWriting(new File(fileName), data);
    }


    public static void executeWriting(File file, String data) {
        System.out.println("> WRITE IN  " + Thread.currentThread().getName());
        waiting();
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardOpenOption.APPEND)) {
            for(String libne : data.split("\n")){
                writer.write(libne);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("> WRITE OUT " + Thread.currentThread().getName());
    }
}
