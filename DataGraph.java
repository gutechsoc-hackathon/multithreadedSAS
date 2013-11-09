import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class DataGraph{
    static ExecutorService threadPool = Executors.newFixedThreadPool(Globals.threads);
    static ConcurrentLinkedDeque<Node> stack = new ConcurrentLinkedDeque<Node>();

    //TODO remainder creation can be tweaked for performance
    //TODO folks on SO say to look at ConcurrentHashMap for performance.
    //TODO here's an idea -- you can split remainder into t hashtables, where t is the number of available threads. And then every thread can do marking/ etc in it
    Set<Node> remainder;
    Set<Node> scc;
    Set<Node> predecessors;
    Set<Node> descendants;
    
    public DataGraph(){
        remainder = Collections.synchronizedSet(new HashSet<Node>(Globals.hashMapCap));
        scc = Collections.synchronizedSet(new HashSet<Node>(Globals.hashMapCap));
        predecessors = Collections.synchronizedSet(new HashSet<Node>(Globals.hashMapCap));
        descendants = Collections.synchronizedSet(new HashSet<Node>(Globals.hashMapCap));
    }

    public DataGraph(Set<Node> remainder){
        this.remainder = remainder;
        //TODO adjust the size?
        scc = Collections.synchronizedSet(new HashSet<Node>(remainder.size()));
        predecessors = Collections.synchronizedSet(new HashSet<Node>(remainder.size()));
        descendants = Collections.synchronizedSet(new HashSet<Node>(remainder.size()));
    }
    
    public DataGraph(Iterable<Node> nodes){
        remainder = Collections.synchronizedSet(new HashSet<Node>(Globals.hashMapCap));
        for (Node node : nodes){
            remainder.add(node);
        }
    }
    
    public DataGraph(Node[] nodes){
        for (Node node : nodes){
            remainder.add(node);
        }
    }
    
    public void addNode(Node node){
        node.reset(this);
        this.remainder.add(node);
    }
    
    /**
     * @param start -- this should be started immediately when threadPool spins a new thread. The job should be accessible from the stack if the stack is empty. The threadPool should wait for all threads to finish and once they do check for emptiness of stack. Or something like that. Thread safe. 
     */
    public void depthSearchPredecessors(Node start){
        Node current = start;
        while(current.mark_predecessor(this)){
            current = current.children.get(0);
            for(int i = 1; i < current.children.size(); i++){
                stack.add(current.children.get(i));
                stack.notifyAll();
            }
        }
        //here I should say something like "finished";
    }
    
}