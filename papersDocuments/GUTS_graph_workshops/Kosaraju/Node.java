package GUTS_graph_workshops.Kosaraju;

import java.util.ArrayList;
class Node{
	final int m_id;
	boolean m_visited;
	int m_group;
	ArrayList<Node> m_adjacent;
    ArrayList<Node> m_revAdjacent;

    public Node(int id){
        m_id = id;
        m_adjacent = new ArrayList<Node>();
        m_revAdjacent = new ArrayList<Node>();
        m_visited = false;
    }
    
    protected void add_adjacent(Node node){
    	m_adjacent.add(node);
    }
    protected void add_reverse(Node node){
    	m_revAdjacent.add(node);
    }

    public void visit(){
    	m_visited = true;
    }
    
    public void group(int node_id){
    	m_group = node_id;
    }
    
    public void reset(){
    	m_visited = false;
    }
    
    public void add_out(Node to){
    	m_adjacent.add(to);
    	to.add_reverse(this);
    }
    
    public void add_in(Node from){
    	from.add_adjacent(this);
    	add_reverse(from);
    }

}
