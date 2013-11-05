import java.util.Hashtable;
import java.util.Stack;

class DataGraph{
	Hashtable<Integer, Node> index;
	Stack<Node> KosarajuStack = new Stack<Node>();
	
	public DataGraph(){
		index = new Hashtable<Integer, Node>();
		KosarajuStack = new Stack<Node>();
	};

	public void addNode(Node node){
		index.put(node.m_id, node);
	}
	
	
	
	public void DepthFirstSearchRev(){
		//Node first = index.elements().nextElement();
		Node first = index.get(1);
		Stack<Node> following = new Stack<Node>();
		following.push(first);
		first.visit();
		outer:
		while(!following.isEmpty()){
			Node next = following.peek();
			for(Node adjacent: next.m_reverse_adjacent){
				if(!adjacent.m_visited){
					System.out.println(adjacent.m_id);
					adjacent.visit();
					following.push(adjacent);
					continue outer;
				}
			}
			following.pop();
		}
	}
	/**
	 * This potentially .m_id);
		for(Node adjacent: node.m_adjacent){
	 */
	public void recursiveKosaraju(Node node){
		node.visit();
		System.out.println(node.m_id);
		for(Node adjacent: node.m_adjacent){
			if (!adjacent.m_visited){
				recursiveKosaraju(adjacent);
			}
		}
		KosarajuStack.push(node);
	}
	
	public void DepthFirstSearch(){
		//Node first = index.elements().nextElement();
		Node first = index.get(1);
		Stack<Node> following = new Stack<Node>();
		following.push(first);
		first.visit();

		outer:
		while(!following.isEmpty()){
			Node next = following.peek();
			for(Node adjacent: next.m_adjacent){
				if(!adjacent.m_visited){
					adjacent.visit();
					System.out.println(next.m_id);
					following.push(adjacent);
					continue outer;
				}

			}
			KosarajuStack.push(following.pop());
		}
	}
}
