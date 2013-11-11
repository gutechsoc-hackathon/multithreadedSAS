import java.util.Random;


public class GraphFactory {
    //TODO I don't like that Globals class anymore. Everything should go into the constructors of this factory.
    public GraphFactory(){
        
    }
    public static DataGraph makeRandomGraph(){
        Random generator = new Random();
        //int size = Globals.problemSizeReal;
        DataGraph dg = new DataGraph(); 
        int size = 12;
        Node[] nodes = new Node[Globals.problemSizeReal];
        for(int i = 0; i < size; i++){
            Node newNode = new Node(i + 1);
            nodes[i] = newNode;
            dg.addNode(newNode);
        }

        for(int i = 0; i < size; i++){
            for(int ii = 0; ii < size; ii++){
                if(generator.nextBoolean()){
                    nodes[i].connectChild(nodes[ii]);
                    System.out.println("connected " + nodes[i].id + " with " + nodes[ii].id);
                }
            }
        }
        return dg; 
    }
    public static DataGraph makeSanityCheckGraph(){
        DataGraph dg = new DataGraph();
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

        dg.addNode(n1);
        dg.addNode(n2);
        dg.addNode(n3);
        dg.addNode(n4);
        dg.addNode(n5);
        dg.addNode(n6);
        dg.addNode(n7);
        dg.addNode(n8);
        dg.addNode(n9);
        dg.addNode(n10);
        dg.addNode(n11);

        n1.connectChild(n2);
        n2.connectChild(n3);
        n2.connectChild(n7);

        n3.connectChild(n4);
        n3.connectChild(n6);
        n4.connectChild(n5);
        n5.connectChild(n6);
        n6.connectChild(n4);
        n7.connectChild(n1);
        n7.connectChild(n2);
        n7.connectChild(n9);
        n8.connectChild(n6);
        n8.connectChild(n10);
        n8.connectChild(n11);
        n9.connectChild(n8);     
        n10.connectChild(n5);
        n10.connectChild(n11);
        n11.connectChild(n9);
        return dg;
    }
}
