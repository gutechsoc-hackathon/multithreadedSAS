import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;


public class NodeHashMap{
    ConcurrentHashMap<Long, Node>[] maps;
    int threadsNumber;
    @SuppressWarnings("unchecked")
    public NodeHashMap(int threadsNumber, int size, float loadFactor){
        maps = new ConcurrentHashMap[threadsNumber];
        for(int i = 0; i < threadsNumber; i++){
            maps[i] = new ConcurrentHashMap<Long, Node>(size, loadFactor, 1);
        }
        this.threadsNumber = threadsNumber;
    }
    public void add(Node node){
        int prehash = (int)(node.id % threadsNumber);
        maps[prehash].put(node.id, node);
    }
    public Node remove(Node node){
        int prehash = (int)(node.id % threadsNumber);
        return maps[prehash].remove(node.id);
    }
    public int size(){
        int sum = 0;
        for (int i = 0; i<threadsNumber; i++){
            sum += maps[i].size();
        }
        return sum;
    }
    
    public void toArray(Node[] array){
        int oi = 0;
        for(int i = 0; i < threadsNumber; i++){
            Enumeration<Long> enumeration = maps[i].keys();
            while(enumeration.hasMoreElements()){
                Node nextNode = maps[i].get(enumeration.nextElement());
                array[oi] = nextNode;
                oi++;
            }
        }
    }
    


}