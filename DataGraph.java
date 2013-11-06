import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class DataGraph{
    Set<Node> reminder = Collections.synchronizedSet(new HashSet<Node>());
    Set<Node> scc = Collections.synchronizedSet(new HashSet<Node>());
    Set<Node> predecessors = Collections.synchronizedSet(new HashSet<Node>());
    Set<Node> descendants = Collections.synchronizedSet(new HashSet<Node>());
}
