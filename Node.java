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
    
    //TODO this can be tweaked in many ways. I'd like to see it merged with mark_descendants. I'm afraid to touch at the moment, since my testcases aren't very powerful, and there's nothing simpler than poison your code with race conditions.
    public synchronized boolean mark_predecessor(DataGraph graph){
       /* {
            java.lang.Thread.sleep(7);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        if(predecessor == false && graph.remainder.contains(this) || graph.descendants.contains(this)){
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
        if(descendant == false && graph.remainder.contains(this) || graph.predecessors.contains(this)){
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
