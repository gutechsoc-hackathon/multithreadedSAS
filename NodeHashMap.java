/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 by Adam Kurkiewicz
 * you can contact me e-mail at: adam /at\ kurkiewicz /dot\ pl
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software. 
 */
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

//TODO this has to support node iteration
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