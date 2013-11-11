import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;


    class ExploreDescendants implements Runnable{
        static AtomicInteger counter = new AtomicInteger(1);
        ExecutorService executor;
        Node start;
        DataGraph dg;
        ExploreDescendants(ExecutorService executor, Node start, DataGraph dg){
            this.executor = executor;
            this.start = start;
            this.dg = dg;
        }
        @Override
        public void run(){
            Node current = start;
            while(current.mark_descendant(dg)){
                //System.out.println("thread" + current);
                for(int i = 1; i < current.children.size(); i++){
                    counter.incrementAndGet();
                    executor.execute(new ExplorePredecessor(executor, current.children.get(i), dg));
                }
                if (current.children.size() > 0) current = current.children.get(0);
            }
            counter.decrementAndGet();
            synchronized (counter) {
                counter.notifyAll();
            }
        }
    }
