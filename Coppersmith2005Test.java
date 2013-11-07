import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;


public class Coppersmith2005Test {
    @Test
    /**
     * That's another test, which looks for possible race condition when there's a lot of threads pushing to one hashset.
     */
    public void multiThreads() throws InterruptedException{
        final DataGraph multiGraph = new DataGraph();
        int size = Globals.problemSize;
        for(int i = 1; i <= size; i++){
            int id = i;
            multiGraph.remainder.add(new Node(id));
        }
    
        Node[] remainderFlat = new Node[multiGraph.remainder.size()];
        multiGraph.remainder.toArray(remainderFlat);
        List<Node> pullList = Arrays.asList(remainderFlat);
    
        Collections.shuffle(pullList);

        int numberOfThreads = 180;      
        Random generator = new Random();
        
        final int partition[] = new int[numberOfThreads + 1];
        partition[0] = 0;
        for(int i = 1; i < numberOfThreads; i++){
            partition[i] = generator.nextInt(Globals.problemSize + 1);
        }
        
        partition[numberOfThreads] = Globals.problemSize;
        Arrays.sort(partition);
        
        abstract class PreDesThread extends Thread{
            int start;
            int stop;
            List<Node> nodes;
            public PreDesThread(int start, int stop, List<Node> nodes){
                super();
                this.start = start;
                this.stop = stop;
                this.nodes = nodes;
            }
        }
        
        class PreThread extends PreDesThread{
            public PreThread(int start, int stop, List<Node> nodes) {
                super(start, stop, nodes);
            }
            @Override
            public void run(){
                for(int i = this.start; i < this.stop; i++){
                    //System.out.println(nodes.get(i).id);
                    nodes.get(i).mark_predecessor(multiGraph);
                }                
            }
        }
    
        class DesThread extends PreDesThread{
            public DesThread(int start, int stop, List<Node> nodes) {
                super(start, stop, nodes);
            }
            @Override
            public void run(){
                for(int i = this.start; i < this.stop; i++){
                    //System.out.println(nodes.get(i).id);
                    nodes.get(i).mark_descendant(multiGraph);
                }                
            }
        }
    
        PreThread[] predecessorThreads = new PreThread[numberOfThreads]; 
        DesThread[] descendantsThreads = new DesThread[numberOfThreads]; 
    
        for(int i = 0; i < numberOfThreads; i++){
            predecessorThreads[i] = new PreThread(partition[i], partition[i + 1], pullList);
        }
    
        for(int i = 0; i < numberOfThreads; i++){
            descendantsThreads[i] = new DesThread(partition[i], partition[i + 1], pullList);
        }
        Assert.assertTrue(multiGraph.remainder.size() + multiGraph.predecessors.size() + multiGraph.descendants.size() + multiGraph.scc.size() == Globals.problemSize);
        
        //some more assertions
                
        Long[] entire = new Long[Globals.problemSize];
        
        for(PreThread thread: predecessorThreads){
            thread.start();
        }
        for(DesThread thread: descendantsThreads){
            thread.start();
        }
        for(PreThread thread: predecessorThreads){
            thread.join();
        }
        for(DesThread thread: descendantsThreads){
            thread.join();
        }

        
        int ii = 0;
        for(Node node: multiGraph.scc){
            //System.out.println(node.id);
            entire[ii] = node.id;
            ii ++;
        }
        
        Arrays.sort(entire);
        Long[] compare = new Long[Globals.problemSize];
        
        for(long li = 0; li < Globals.problemSize; li++){
            compare[(int)li] = li + 1;
        }
        
        Assert.assertArrayEquals(entire, compare);
        
    }
    @Test
    /**
     * this is a comprehensive, randomized test, which runs two threads, one for predecessors and one for descendants. The testing looks at the following properties:
     * are all pulled descendants either in scc or in descendants?
     * are all pulled predecessors either in scc or in predecessors?
     * is the partition of the set, once merged back into one set, the same set? (roughly).
     * Anyway, do not trust these tests. Write your own ones. 
     * @throws InterruptedException
     */
    public void twoThreads() throws InterruptedException {
        final DataGraph multiGraph = new DataGraph();
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
        
        //System.out.println(Globals.problemSize);
        //System.out.println(multiGraph.remainder.size());
        //System.out.println(multiGraph.scc.size());
        Assert.assertTrue(multiGraph.remainder.size() + multiGraph.predecessors.size() + multiGraph.descendants.size() + multiGraph.scc.size() == Globals.problemSize);
        
        //some more assertions
        
        Node[] pushPre = new Node[pullPre.length];
        Node[] pushDes = new Node[pullDes.length];
        int ii = 0;
        for(Node node: multiGraph.predecessors){
            pushPre[ii] = node;
            ii ++;
        }
        
        for(Node node: multiGraph.scc){
            pushPre[ii] = node;
            ii ++;
        }

        ii = 0;
        for(Node node: multiGraph.descendants){
            pushDes[ii] = node;
            ii ++;
        }

        for(Node node: multiGraph.scc){
            pushDes[ii] = node;
            ii++;
        }

        Arrays.sort(pullPre);
        Arrays.sort(pushPre);
        Arrays.sort(pullDes);
        Arrays.sort(pushDes);

        Assert.assertArrayEquals(pullPre, pushPre);
        Assert.assertArrayEquals(pullDes, pushDes);
        
        Long[] entire = new Long[Globals.problemSize];
        
        ii = 0;

        for(Node node: multiGraph.predecessors){
            entire[ii] = node.id;
            ii ++;
        }

        for(Node node: multiGraph.descendants){
            entire[ii] = node.id;
            ii ++;
        }


        for(Node node: multiGraph.scc){
            entire[ii] = node.id;
            ii ++;
        }

        for(Node node: multiGraph.remainder){
            entire[ii] = node.id;
            ii ++;
        }
        
        Arrays.sort(entire);
        Long[] compare = new Long[Globals.problemSize];
        
        for(long li = 0; li < Globals.problemSize; li++){
            compare[(int)li] = li + 1;
        }
        
        Assert.assertArrayEquals(entire, compare);
    }

}
