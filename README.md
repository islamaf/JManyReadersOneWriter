# JManyReadersOneWriter

## Introducction

Java implementation of many readers one writer problem, ready to use.

See discussion here: https://codereview.stackexchange.com/questions/216603/many-readers-one-writer-with-semaphores-and-multithreading

## Howto

Well, as it is apache licence, you may use as you want.  
The preferred method would be to inherit  `org.obapanel.jmanyreadersonewriter.AbsrtactReadersWriter` and write the critical sections on the abstract methods `executeReading` and `executeWriting`.  
Then instantiate the class and call `read` and `write` as you need.

See examples on `org.obapanel.jmanyreadersonewriter.example.ExampleFileReadersWriter`


## Source

Original ides was taken from this original discussion: https://codereview.stackexchange.com/questions/127234/reader-writers-problem-using-semaphores-in-java?newreg=afcfc71833ab4152b20bffdaccd175d7

And from this wikipedia entry: https://en.wikipedia.org/wiki/Readers%E2%80%93writers_problem


Other implementations I took a look:
* https://github.com/oscarcosta/readers-writers
* https://github.com/Spirid/Java-DemoReaders-writers-problem
* https://github.com/omartoutounji/readers-writers-problem
* https://github.com/lmiguelmh/readers-writers-app/tree/master/dev/readers-writers-app/src/main/java/pucp/pl/readers

## Disclaimer

This library is done as an programming exercice, but you could use the basic java reader writer implementation  
https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/ReadWriteLock.html  


