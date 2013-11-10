import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class DataGraph{
    static ExecutorService threadPool = Executors.newFixedThreadPool(Globals.threads);

    //TODO remainder creation can be tweaked for performance
    //TODO folks on SO say to look at ConcurrentHashMap for performance.
    //TODO here's an idea -- you can split remainder into t hashtables, where t is the number of available threads. And then every thread can do marking/ etc in it
    
    NodeHashMap remainder;
    NodeHashMap scc;
    NodeHashMap predecessors;
    NodeHashMap descendants;
    
    public DataGraph(){
        remainder = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
        scc = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
        predecessors = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
        descendants = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
    }

    public DataGraph(NodeHashMap remainder){
        this.remainder = remainder;
        //TODO adjust the size?
        scc = new NodeHashMap(Globals.threads, Globals.sizeToCap(remainder.size()), Globals.loadFactor);
        predecessors = new NodeHashMap(Globals.threads, Globals.sizeToCap(remainder.size()), Globals.loadFactor);
        descendants = new NodeHashMap(Globals.threads, Globals.sizeToCap(remainder.size()), Globals.loadFactor);
    }
    
    public DataGraph(Iterable<Node> nodes){
        remainder = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
        for (Node node : nodes){
            remainder.add(node);
        }
    }
    
    /*public DataGraph(Node[] nodes){
        remainder = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
        for (Node node : nodes){
            remainder.add(node);
        }
    }*/
    
    public void addNode(Node node){
        node.reset(this);
        this.remainder.add(node);
    }
    

    /**
     * @param start -- this should be started immediately when threadPool spins a new thread. The job should be accessible from the stack if the stack is empty. The threadPool should wait for all threads to finish and once they do check for emptiness of stack. Or something like that. Thread safe. 
     */
    
}