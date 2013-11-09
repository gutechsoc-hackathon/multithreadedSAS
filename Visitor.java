
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


public class Visitor{
    
    class VisitorThread extends Thread{
        ConcurrentHashMap<Long, Node> chm;
        DataGraph dg;
        public VisitorThread(ConcurrentHashMap<Long, Node> chm, DataGraph dg){
            super();
            this.chm = chm;
            this.dg = dg;
        }
        @Override
        public void run(){
            Enumeration<Long> enumeration = chm.keys();
            while(enumeration.hasMoreElements()){
                chm.get(enumeration.nextElement()).reset(dg);
            }
            finishedCounter.countDown();
        }
    }

    final int threadsNumber;
    DataGraph dg;
    VisitorThread[] threads;
    CountDownLatch finishedCounter;

    public Visitor(int threadsNumber, DataGraph dg){
        this.dg = dg;
        threads = new VisitorThread[threadsNumber];
        this.threadsNumber = threadsNumber;
        finishedCounter = new CountDownLatch(threadsNumber);
    }
    
    public void newJob(NodeHashMap nhm){
        assert(nhm.threadsNumber == this.threadsNumber);
        int i = 0;
        for(ConcurrentHashMap<Long, Node> map: nhm.maps){
            threads[i] = new VisitorThread(map, dg);
        }
    }
    
    public void visitAll(){
        for(Thread thread : threads){
            thread.start();
        }
        try {
            finishedCounter.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

