import java.util.ArrayList;

class Node implements Comparable<Node>{
	final long id;
	boolean predecessor;
	boolean descendant;
	DataGraph graph;
	ArrayList<Node> children = new ArrayList<Node>(Globals.childrenThreshold);
	ArrayList<Node> parents = new ArrayList<Node>(Globals.childrenThreshold);
	
	/**
	 * @param id id == 0 will break your code. You have been warned.
	 */
	
    public Node(long id){
        assert(id != 0);
        this.id = id;
        predecessor = false;
    	descendant = false;
    	graph = null;

    }
    
    /**
     * This isn't thread safe, use it only once, when you prepare the graph.
     */
    public void connectChild(Node child){
        child.parents.add(this);
        this.children.add(child);
    }
    
    public synchronized void reset(DataGraph graph){
        this.graph = graph;
        predecessor = false;
        descendant = false;
        notifyAll();
        return;
    }
    
    //TODO this can be tweaked in many ways. There's some redundancy. Also, I'd like to see it merged with mark_descendants. I'm afraid to touch it at the moment, since my testcases aren't very powerful, and there's nothing simpler than poison your code with race conditions.
    public synchronized boolean mark_predecessor(DataGraph graph){
       /* {
            java.lang.Thread.sleep(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        
        //TODO by correcting a concurrency bug all the tests have been screwed up. Well done.
        if(predecessor == false && graph == this.graph){
            predecessor = true;
            if(descendant == true){
                graph.scc.add(this);
                graph.descendants.remove(this);
            } else{
                graph.predecessors.add(this);
                graph.remainder.remove(this);
            }
            notifyAll();
            return true;
        } else{
            notifyAll();
            return false;
        }
    }
    
    public synchronized boolean mark_descendant(DataGraph graph){
        /*try {
            java.lang.Thread.sleep(3);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        if(descendant == false && graph == this.graph){
            descendant = true;
            if(predecessor == true){
                graph.scc.add(this);
                graph.predecessors.remove(this);
            } else{
                graph.descendants.add(this);            
                graph.remainder.remove(this);
            }
            notifyAll();
            return true;
        } else{
            notifyAll();
            return false;
        }
    }
    
    @Override
    public int compareTo(Node node) {
        return Long.compare(this.id, node.id);
    }
}
