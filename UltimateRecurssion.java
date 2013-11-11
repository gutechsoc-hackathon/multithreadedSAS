import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;


public class UltimateRecurssion implements Runnable{
    static AtomicInteger counter = new AtomicInteger(1);
    static LinkedList<NodeHashMap> solutions = new LinkedList<NodeHashMap>();
    
    ExecutorService executor;
    DataGraph startDG;
    Visitor visitor;
    UltimateRecurssion(ExecutorService executor, Visitor visitor, DataGraph startDG){
        solutions = new LinkedList<NodeHashMap>();
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
        if (startDG.remainder.size() == 1){
            Node[] array = new Node[1];
            startDG.remainder.toArray(array);
            array[0].reset(startDG);
            UltimateRecurssion.solutions.add(startDG.remainder);
            counter.decrementAndGet();
            return;
        }
        visitor.setDG(startDG);
        visitor.newJob(startDG.remainder);
        visitor.visitAll();
        ExplorePredecessor ep = new ExplorePredecessor(executor, startDG.remainder.maps[0].get(startDG.remainder.maps[0].keys().nextElement()), startDG);
        ExploreDescendants ed = new ExploreDescendants(executor, startDG.remainder.maps[0].get(startDG.remainder.maps[Globals.threads - 1].keys().nextElement()), startDG);
        executor.execute(ep);
        executor.execute(ed);
        synchronized (ExplorePredecessor.counter){
            while (ExplorePredecessor.counter.get() != 0){
                try {
                    ExplorePredecessor.counter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        synchronized (ExploreDescendants.counter){
            while (ExploreDescendants.counter.get() != 0){
                try {
                    ExploreDescendants.counter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if(startDG.descendants.size() > 0){
            counter.incrementAndGet();
            new UltimateRecurssion(executor, visitor, new DataGraph(startDG.predecessors));
        }
        
        if(startDG.descendants.size() > 0){
            counter.incrementAndGet();
            new UltimateRecurssion(executor, visitor, new DataGraph(startDG.remainder));
        }

        if(startDG.descendants.size() > 0){
            counter.incrementAndGet();
            new UltimateRecurssion(executor, visitor, new DataGraph(startDG.descendants));
        }

        solutions.push(startDG.scc);
        counter.decrementAndGet();
        synchronized (counter) {
            counter.notifyAll();
        }
    }
}
