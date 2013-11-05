import java.util.ArrayList;
class Node{
	final int m_id;
	boolean m_visited;
	boolean m_revVisited;
	ArrayList<Node> m_adjacent;
    ArrayList<Node> m_reverse_adjacent;

    public Node(int id){
        m_id = id;
        m_adjacent = new ArrayList<Node>();
        m_reverse_adjacent = new ArrayList<Node>();
        m_visited = false;
    }
    
    protected void add_adjacent(Node node){
    	m_adjacent.add(node);
    }
    protected void add_reverse(Node node){
    	m_reverse_adjacent.add(node);
    }

    public void visit(){
    	m_visited = true;
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
