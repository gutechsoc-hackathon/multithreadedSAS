import java.util.ArrayList;
class Node{

	final int m_id;
	ArrayList<Node> m_adjacent;
    ArrayList<Node> m_reverse_adjacent;

    public Node(int id){
        m_id = id;
        m_adjacent = new ArrayList<Node>();
        m_reverse_adjacent = new ArrayList<Node>();
    }
    
    protected void add_adjacent(Node node){
    	m_adjacent.add(node);
    }
    
    public void add_out(Node to){
    	m_adjacent.add(to);
    }

}
