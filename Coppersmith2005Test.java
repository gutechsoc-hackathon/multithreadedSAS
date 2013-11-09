import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Test;


public class Coppersmith2005Test {
    public static int bigHashMapSize = 85000; // should create 680 MB size object; Will the object creation scale?
    @Test
    public void createBigHashMap(){
        Random generator = new Random();
        ConcurrentHashMap<Node, Boolean> map = new ConcurrentHashMap<Node, Boolean>(bigHashMapSize);
        
        for (int i = 0; i <= 1; i++){
            map.put(new Node(generator.nextLong()), true);
        }
        map.keys().nextElement();
        System.out.println(map.containsKey(7));
        
    }
    
    @Test
    public void repeatElements(){
        Random generator = new Random();
        final int test_rep = 30;
        final int nodeNumber = 20;
        final int thread_number = 8;
        final int test_length = 100;
        
        class PreDesThread extends Thread{
            int track;
            boolean[][] operation;
            int[][] order;
            Node[] nodes;
            DataGraph dg;
            public PreDesThread(int track, boolean[][] operation, int[][] order, Node[] nodes, DataGraph dg){
                super();
                this.track = track;
                this.operation = operation;
                this.order = order;
                this.dg = dg;
                this.nodes = nodes;
            }
            @Override
            public void run(){
                for(int i = 0; i < test_length; i++){
                    Node node = nodes[order[track][i]];
                    //System.out.print(node.id);
                    
                    if(operation[track][i]){
                        node.mark_predecessor(dg);
                    } else {
                        node.mark_descendant(dg);
                    }
                            
                }                
            }

        }
        
        for(int i = 0; i < test_rep; i++){
            final DataGraph dg = new DataGraph();
            final Node[] nodes = new Node[nodeNumber];
            final int[][] order = new int[thread_number][test_length];
            final boolean[][] operation = new boolean[thread_number][test_length];
            PreDesThread[] threads = new PreDesThread[thread_number];
            
            for(int ii = 1; ii <= nodeNumber; ii++){
                nodes[ii - 1] = new Node(ii);
                dg.addNode(nodes[ii - 1]);
            }

            for(int ti = 0; ti < thread_number; ti++){
                for(int tii = 0; tii < test_length; tii++){
                    order[ti][tii] = generator.nextInt(3);
                }
            }
            
            for(int ti = 0; ti < thread_number; ti++){
                for(int tii = 0; tii < test_length; tii++){
                    operation[ti][tii] = generator.nextBoolean();
                }
            }
            
            for(int ti = 0; ti < thread_number; ti++){
                threads[ti] = new PreDesThread(ti, operation, order, nodes, dg);
            }
            
            for(int ti = 0; ti < thread_number; ti++){
                threads[ti].start();
            }
                   
        }

    }
    @Test
    public void sanityCheck(){
        DataGraph dg = new DataGraph();
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        
        dg.addNode(n1);
        dg.addNode(n2);
        dg.addNode(n3);
        dg.addNode(n4);
        dg.addNode(n5);
        
        n1.mark_predecessor(dg);
        n2.mark_predecessor(dg);
        n3.mark_descendant(dg);
        n2.mark_predecessor(dg);
        n1.mark_descendant(dg);
        
        Node[] scc = new Node[1];
        dg.scc.toArray(scc);
        Assert.assertTrue(scc.length == 1);
        Assert.assertTrue(scc[0].id == 1);

        Node[] descendants = new Node[1];
        dg.descendants.toArray(descendants);
        Assert.assertTrue(descendants.length == 1);
        Assert.assertTrue(descendants[0].id == 3);

        Node[] predecessors = new Node[1];
        dg.predecessors.toArray(predecessors);
        Assert.assertTrue(dg.predecessors.size() == 1);
        Assert.assertTrue(predecessors[0].id == 2);

        Node[] remainder = new Node[2];
        dg.remainder.toArray(remainder);
        Assert.assertTrue(dg.remainder.size() == 2);
        Arrays.sort(remainder);
        Assert.assertTrue(remainder[0].id == 4);
        Assert.assertTrue(remainder[1].id == 5);
    }
    @Test
    /**
     * That's another test, which looks for possible race condition when there's a lot of threads pushing to one hashset.
     */
    public void multiThreads() throws InterruptedException{
        final DataGraph multiGraph = new DataGraph();
        int size = Globals.problemSizeTests;
        for(int i = 1; i <= size; i++){
            int id = i;
            multiGraph.addNode(new Node(id));
        }
    
        Node[] remainderFlat = new Node[multiGraph.remainder.size()];
        multiGraph.remainder.toArray(remainderFlat);
        List<Node> pullList = Arrays.asList(remainderFlat);
    
        Collections.shuffle(pullList);

        int numberOfThreads = 30;      
        Random generator = new Random();
        
        final int partition[] = new int[numberOfThreads + 1];
        partition[0] = 0;
        for(int i = 1; i < numberOfThreads; i++){
            partition[i] = generator.nextInt(Globals.problemSizeTests + 1);
        }
        
        partition[numberOfThreads] = Globals.problemSizeTests;
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
        Assert.assertTrue(multiGraph.remainder.size() + multiGraph.predecessors.size() + multiGraph.descendants.size() + multiGraph.scc.size() == Globals.problemSizeTests);
        
        //some more assertions
                
        Long[] entire = new Long[Globals.problemSizeTests];
        
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
        Long[] compare = new Long[Globals.problemSizeTests];
        
        for(long li = 0; li < Globals.problemSizeTests; li++){
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
        int size = Globals.problemSizeTests;
        int pullPreSize = Globals.problemSizeTests/2;
        int pullDesSize = Globals.problemSizeTests/2;
        for(int i = 1; i <= size; i++ ){
            int id = i; //generator.nextLong()
            multiGraph.addNode(new Node(id));
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
        Assert.assertTrue(multiGraph.remainder.size() + multiGraph.predecessors.size() + multiGraph.descendants.size() + multiGraph.scc.size() == Globals.problemSizeTests);
        
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
        
        Long[] entire = new Long[Globals.problemSizeTests];
        
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
        Long[] compare = new Long[Globals.problemSizeTests];
        
        for(long li = 0; li < Globals.problemSizeTests; li++){
            compare[(int)li] = li + 1;
        }
        
        Assert.assertArrayEquals(entire, compare);
    }

}
