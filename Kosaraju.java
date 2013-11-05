class Kosaraju{
	
    public static void main(String[] args){
    	Node n1 = new Node(1);
    	Node n2 = new Node(2);
    	Node n3 = new Node(3);
    	Node n4 = new Node(4);
    	Node n5 = new Node(5);
    	Node n6 = new Node(6);
    	Node n7 = new Node(7);
    	Node n8 = new Node(8);
    	Node n9 = new Node(9);
    	Node n10 = new Node(10);
    	Node n11 = new Node(11);

    	n1.add_out(n2);
    	n2.add_out(n3);
    	n2.add_out(n7);

    	n3.add_out(n4);
    	n3.add_out(n6);
    	n4.add_out(n5);
    	n5.add_out(n6);
    	n6.add_out(n4);
    	n7.add_out(n1);
    	n7.add_out(n2);
    	n7.add_out(n9);
    	n8.add_out(n6);
    	n8.add_out(n10);
    	n8.add_out(n11);
      	n9.add_out(n8);  	
    	n10.add_out(n5);
    	n10.add_out(n11);
    	n11.add_out(n9);
    	
    	DataGraph mydata = new DataGraph();
    	mydata.addNode(n1);
    	mydata.addNode(n2);
    	mydata.addNode(n3);
    	mydata.addNode(n4);
    	mydata.addNode(n5);
    	mydata.addNode(n6);
    	mydata.addNode(n7);
    	mydata.addNode(n8);
    	mydata.addNode(n9);
    	mydata.addNode(n10);
    	mydata.addNode(n11);
    	mydata.DepthFirstSearch();
    	
    	for(Node node: mydata.KosarajuStack){
    		System.out.println(node.m_id);
    	}

    }
    
}

