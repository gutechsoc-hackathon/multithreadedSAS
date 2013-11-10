
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;


public class Coppersmith2005{
    
    static class VisitorThread extends Thread{
        ConcurrentHashMap<Long, Node> chm;
        DataGraph dg;
        public VisitorThread(ConcurrentHashMap<Long, Node> chm, DataGraph dg){
            super();
            this.chm = chm;
            this.dg = dg;
        }
        @Override
        public void run(){
            Enumeration<Long> enumeration = chm.keys();
            while(enumeration.hasMoreElements()){
                chm.get(enumeration.nextElement()).reset(dg);
            }
        }
    }
    
    public static void main(String[] args){

        int size = 2000000;
        int numberOfThreads = 2;
        DataGraph dg = new DataGraph();
        VisitorThread[] threadpool = new VisitorThread[numberOfThreads];
        @SuppressWarnings("unchecked")
        ConcurrentHashMap<Long, Node>[] maps = new ConcurrentHashMap[numberOfThreads];
        
        final long createMapStart = System.currentTimeMillis();
            for (int i = 0; i < numberOfThreads; i++){
                maps[i] =  new ConcurrentHashMap<Long, Node>((int) (size / 0.75 + 1000), (float) 0.75, 1);
            }
        final long createMapStop = System.currentTimeMillis();
        
        final long createNodesStart = System.currentTimeMillis();
            for(int i = 1; i <= size; i++){
                Node newNode = new Node(i);
                int prehash = i % numberOfThreads;
                maps[prehash].put(newNode.id, newNode);
            }
        final long createNodesStop = System.currentTimeMillis();
                
        final long iterateNodesStart = System.currentTimeMillis();
            for(int i = 0; i < numberOfThreads; i++){
                threadpool[i] = new VisitorThread(maps[i], dg);
                threadpool[i].start();
            }
            for(int i = 0; i < numberOfThreads; i++){
                try {
                    threadpool[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        final long iterateNodesStop = System.currentTimeMillis();
        System.out.println("milis for createMap = " + (createMapStop - createMapStart));
        System.out.println("milis for createNodes = " + (createNodesStop - createNodesStart));
        System.out.println("milis for iterateNodes = " + (iterateNodesStop - iterateNodesStart));
    }

}

