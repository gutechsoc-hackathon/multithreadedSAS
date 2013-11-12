import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;


public class UltimateRecurssion implements Runnable{
    static AtomicInteger counter = new AtomicInteger(1);
    static ConcurrentLinkedDeque<NodeHashMap> solutions = new ConcurrentLinkedDeque<NodeHashMap>();
    
    ExecutorService executor;
    DataGraph startDG;
    Visitor visitor;
    UltimateRecurssion(ExecutorService executor, Visitor visitor, DataGraph startDG){
        solutions = new ConcurrentLinkedDeque<NodeHashMap>();
        this.visitor = visitor;
        this.startDG = startDG;
        this.executor = executor;
    }
    /**
     * This is the final step in a few days of a hard work. The method run has to:
     * mark all the nodes as belonging to startDG. It has to be done in parallel, that's
     * why I was working on the Visitor class. Later it has to explore all the descendants
     * and all the predecessors. That's why I created ExploreDescendants and ExplorePredecessors.
     * Eventually I have to throw current.scc out to solutions and recur on current.remainder,
     * current.predecessors and current.descendants. Let's see how it works.
     */
    @Override
    public void run(){
    	System.out.println("here");
        if (startDG.remainder.size() == 1){
            Node[] array = new Node[1];
            startDG.remainder.toArray(array);
            array[0].reset(startDG);
            UltimateRecurssion.solutions.push(startDG.remainder);
            counter.decrementAndGet();
            synchronized (counter) {
                counter.notifyAll();
            }
            return;
        }
        visitor.setDG(startDG);
        visitor.newJob(startDG.remainder);
        visitor.visitAll();
        ExplorePredecessor ep = new ExplorePredecessor(executor, startDG.remainder.maps[0].get(startDG.remainder.maps[0].keys().nextElement()), startDG);
        ExploreDescendants ed = new ExploreDescendants(executor, startDG.remainder.maps[0].get(startDG.remainder.maps[0].keys().nextElement()), startDG);
        executor.execute(ed);
        executor.execute(ep);
        
        synchronized (ExplorePredecessor.counter){
            System.out.println("pred conter: " + ExplorePredecessor.counter);

            while (ExplorePredecessor.counter.get() != 0){
                try {
                    //System.out.println(counter.get());
                    ExplorePredecessor.counter.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println("finished pre: ");
        }
        synchronized (ExploreDescendants.counter){
            System.out.println("desc conter: " + ExploreDescendants.counter);
            while (ExploreDescendants.counter.get() != 0){
                try {
                	//System.out.println(counter.get());
                    ExploreDescendants.counter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("finished des ");
        }
        /*if(startDG.descendants.size() > 0){
            counter.incrementAndGet();
            Runnable ur = new UltimateRecurssion(executor, visitor, new DataGraph(startDG.predecessors));
            executor.execute(ur);
        }
        
        if(startDG.descendants.size() > 0){
            counter.incrementAndGet();
            Runnable ur = new UltimateRecurssion(executor, visitor, new DataGraph(startDG.remainder));
            executor.execute(ur);
        }

        if(startDG.descendants.size() > 0){
            counter.incrementAndGet();
            Runnable ur = new UltimateRecurssion(executor, visitor, new DataGraph(startDG.descendants));
            executor.execute(ur);
        }*/

        solutions.push(startDG.scc);
        counter.decrementAndGet();
        synchronized (counter) {
            counter.notifyAll();
        }
    }
}
