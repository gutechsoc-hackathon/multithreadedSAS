import java.util.concurrent.ConcurrentHashMap;


public class NodeHashMap{
    ConcurrentHashMap<Long, Node>[] maps;
    int threadsNumber;
    @SuppressWarnings("unchecked")
    public NodeHashMap(int threadsNumber, int size, float loadFactor){
        maps = new ConcurrentHashMap[threadsNumber];
        for(int i = 0; i < size; i++){
            maps[i] = new ConcurrentHashMap<Long, Node>(size, loadFactor, 1);
        }
    }
    public void add(Node node){
        int prehash = (int)(node.id % threadsNumber);
        maps[prehash].put(node.id, node);
    }
    public Node remove(Node node){
        int prehash = (int)(node.id % threadsNumber);
        return maps[prehash].remove(node.id);
    }
    
}