import java.util.Set;

class Node{
	final int id;
	boolean predecessor;
	boolean descendant;
	int group;
	
	/**
	 * @param id id == 0 will break your code. You have been warned.
	 */
	
    public Node(int id){
        this.id = id;
        predecessor = false;
    	descendant = false;
    	this.group = 0;
    }
    
    public synchronized void mark_predecessor(Set<Node> scc, Set<Node> remainder, Set<Node> predecessors){
        assert(predecessor != true);
        predecessor = true;
        if(descendant == true){
            scc.add(this);
        }
        predecessors.add(this);
        boolean removal = remainder.remove(this);
        assert(removal);
        notifyAll();
    }
    
    public synchronized void mark_descendant(Set<Node> scc, Set<Node> remainder, Set<Node> descendants){
        assert(descendant != true);
        descendant = true;
        if(predecessor == true){
            scc.add(this);
        }
        descendants.add(this);
        boolean removal = remainder.remove(this);
        assert(removal);
        notifyAll();
    }
    
    public synchronized void reset(){
        predecessor = false;
        descendant = false;
        notifyAll();
    }
    
    public synchronized void mark_group(int id){
        group = id;
        notifyAll();
    }

}
