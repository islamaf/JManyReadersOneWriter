package org.obapanel.jmanyreadersonewriter.example;

import org.obapanel.jmanyreadersonewriter.AbsrtactReadersWriter;

import java.io.File;
import java.io.IOException;

public class ExampleFileReadersWriter {




    public static void main(String[] args) throws IOException {
        final FileRW fileRW = new FileRW();
        System.out.println("Temp file " + fileRW.file.getAbsolutePath());
        Thread t01 = new Thread(() -> fileRW.write("hola"));
        t01.setName("t01");
        Thread t02 = new Thread(() -> fileRW.write("que tal\nseñor mio"));
        t02.setName("t02");
        Thread t03 = new Thread(() -> shout(fileRW.read()));
        t03.setName("t03");
        Thread t04 = new Thread(() -> fileRW.write("com esta\nusted hoy\n en este lugar"));
        t04.setName("t04");
        Thread t05 = new Thread(() -> fileRW.write("y com esta\ny usted hoy\ny en este lugar"));
        t05.setName("t05");
        Thread t06 = new Thread(() -> shout(fileRW.read()));
        t06.setName("t06");
        Thread t07 = new Thread(() -> fileRW.write("xy com esta\nxy usted hoy\nxy en este lugar"));
        t07.setName("t07");
        Thread t08 = new Thread(() -> shout(fileRW.read()));
        t08.setName("t08");
        Thread t09 = new Thread(() -> shout(fileRW.read()));
        t09.setName("t09");
        Thread t10 = new Thread(() -> shout(fileRW.read()));
        t10.setName("t10");
        Thread t11 = new Thread(() -> shout(fileRW.read()));
        t11.setName("t11");
        Thread t12 = new Thread(() -> shout(fileRW.read()));
        t12.setName("t12");
        Thread t13 = new Thread(() -> fileRW.write("pues muy bien"));
        t13.setName("t13");
        Thread t14 = new Thread(() -> shout(fileRW.read()));
        t14.setName("t14");
        Thread t15 = new Thread(() -> shout(fileRW.read()));
        t15.setName("t15");
        Thread t16 = new Thread(() -> shout(fileRW.read()));
        t16.setName("t16");

        t01.start();
        t02.start();
        t03.start();
        t04.start();
        t05.start();
        t06.start();
        t07.start();
        t08.start();
        t09.start();
        t10.start();
        t11.start();
        t12.start();
        t13.start();
        t14.start();
        t15.start();
        t16.start();
        System.out.println("ALL T INITED");


        try {
            t01.join();
            t16.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void shout(final String readedFromfileRW) {
        readedFromfileRW.length();
        //System.out.println(Thread.currentThread().getName() + " > " + readedFromfileRW.replaceAll("\n", " ") );
    }

    private static class FileRW extends AbsrtactReadersWriter<String> {


        File file;

        FileRW() throws IOException {
            file = FileReaderWriterMethods.createTempFileWillBeDeletedOnExit();
        }

        FileRW(String filePath){
            file = new File(filePath);
        }

        FileRW(File file){
            this.file = file;
        }

        File getFile(){
            return this.file;
        }


        @Override
        protected String executeReading() {
            return FileReaderWriterMethods.executeReading(file);
        }

        @Override
        protected void executeWriting(String data) {
            FileReaderWriterMethods.executeWriting(file,data);
        }
    }
}
