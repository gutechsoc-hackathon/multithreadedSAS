import java.util.HashMap;

class DataGraph{
	HashMap<Integer, Node> index;
	public DataGraph(){
		index = new HashMap<Integer, Node>();
	};

	public void addNode(Node node){
		index.put(node.m_id, node);
	}
	
	public static void connect(Node from, Node to){
		//from.
	}
		
}
