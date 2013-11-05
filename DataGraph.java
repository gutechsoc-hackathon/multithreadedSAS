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
	
	public void markGroups(){
		while(!KosarajuStack.empty()){
			Node node = KosarajuStack.pop();
			if (node.m_group == 0){
				markGroups(node, node.m_id);
			}
			System.out.println("node: " + node.m_id + " group: " + node.m_group);
		}
	}

	public void markGroups(Node node, int group){
		if(node.m_group != 0) return;
		else{
			node.m_group = group;
			for(Node rev_adjacent: node.m_revAdjacent){
				markGroups(rev_adjacent, group);
			}
		}
	}

	/**	
     * this can be potentially slow/ cause program stack overflow. Better use non-recursive KosarajuStack.
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
