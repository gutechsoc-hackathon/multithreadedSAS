import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

class DataGraph{
    
    Set<Node> remainder = Collections.synchronizedSet(new HashSet<Node>(Globals.hashMapCap));
    ReentrantLock reminderLock = new ReentrantLock();
    Set<Node> scc = Collections.synchronizedSet(new HashSet<Node>(Globals.hashMapCap));
    Set<Node> predecessors = Collections.synchronizedSet(new HashSet<Node>(Globals.hashMapCap));
    Set<Node> descendants = Collections.synchronizedSet(new HashSet<Node>());
    
    DataGraph(){}
    
}