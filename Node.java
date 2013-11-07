import java.util.Set;

class Node implements Comparable<Node>{
	final long id;
	boolean predecessor;
	boolean descendant;
	int group;
	
	/**
	 * @param id id == 0 will break your code. You have been warned.
	 */
	
    public Node(long id){
        assert(id != 0);
        this.id = id;
        predecessor = false;
    	descendant = false;
    	this.group = 0;
    }
    
    public synchronized void mark_predecessor(DataGraph graph){
        assert(predecessor != true);
        predecessor = true;
        if(descendant == true){
            graph.scc.add(this);
            graph.descendants.remove(this);
        } else{
            graph.predecessors.add(this);
        }
        graph.remainder.remove(this);
        notifyAll();
    }
    
    public synchronized void mark_descendant(DataGraph graph){
        assert(descendant != true);
        descendant = true;
        if(predecessor == true){
            graph.scc.add(this);
            graph.predecessors.remove(this);
        } else{
            graph.descendants.add(this);            
        }
        graph.remainder.remove(this);
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

    @Override
    public int compareTo(Node node) {
        return Long.compare(this.id, node.id);
    }
}
