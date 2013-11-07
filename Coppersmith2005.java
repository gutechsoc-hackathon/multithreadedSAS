import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//import java.util.Random;
class Coppersmith2005{
    public static void main(String[] args) throws InterruptedException{
        
        //Random generator = new Random();
	    final DataGraph multiGraph = new DataGraph();
	    final DataGraph singleGraph = new DataGraph();
	    int size = Globals.problemSize;
	    int pullPreSize = Globals.problemSize/2;
	    int pullDesSize = Globals.problemSize/2;
	    for(int i = 1; i <= size; i++ ){
	        int id = i; //generator.nextLong()
	        multiGraph.remainder.add(new Node(id));
	    }

	    Node[] remainderFlat = new Node[multiGraph.remainder.size()];
	    multiGraph.remainder.toArray(remainderFlat);
	    final Node[] pullPre = new Node[pullPreSize];
	    final Node[] pullDes = new Node[pullDesSize];
	    List<Node> pullList = Arrays.asList(remainderFlat);

	    Collections.shuffle(pullList);
	    //System.out.println(multiGraph.reminder.size());
	    //pull predecessors
	    for(int i = 0; i < pullPreSize; i++){
	        pullPre[i] = pullList.get(i);
	    }
        
	    Collections.shuffle(pullList);
        
	    //pull descendants
        for(int i = 0; i < pullDesSize; i++){
            pullDes[i] = pullList.get(i);
        }	    
        
        Thread preThread = new Thread(
                new Runnable(){
                    public void run(){
                        for(Node node: pullPre){
                            node.mark_predecessor(multiGraph);
                        }
                    }
                }
        );

        Thread desThread = new Thread(
                new Runnable(){
                    public void run(){
                        for(Node node: pullDes){
                            node.mark_descendant(multiGraph);
                        }
                    }
                }
        );
        
        preThread.start();
        desThread.start();

        preThread.join();
        desThread.join();
        
        Node[] multiSolution = new Node[multiGraph.scc.size()];
        multiGraph.scc.toArray(multiSolution);
        Arrays.sort(multiSolution);
        
        

        Node[] singleSolution = new Node[multiGraph.scc.size()];
        multiGraph.scc.toArray(singleSolution);
        Arrays.sort(singleSolution);
        System.out.println(Globals.problemSize);
        System.out.println(multiGraph.remainder.size());
        System.out.println(multiGraph.scc.size());
        assert(multiGraph.remainder.size() + multiGraph.predecessors.size() + multiGraph.descendants.size() - multiGraph.scc.size() == Globals.problemSize);
        
        //Let's go back to previous set
        
        //for(Node node: )
        
        // if the line below doesn't fail, then turn on your assertions, source -5 when you compile and -ea when you execute.
        //assert false;
        System.out.println("predecessors");
        for(Node node: pullPre){
            System.out.println(node.id);
        }

        System.out.println("descendants");
        for(Node node: pullDes){
            System.out.println(node.id);
        }

        System.out.println("solution");
        for(Node node: multiSolution){
            System.out.println(node.id);            
        }

        System.out.println("rem");
        for(Node node: multiGraph.remainder){
            System.out.println(node.id);    
        }

        
    }

}

