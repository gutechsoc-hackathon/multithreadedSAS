/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 by Adam Kurkiewicz
 * You can contact me by e-mail at: adam /at\ kurkiewicz /dot\ pl
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

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class DataGraph{
    static ExecutorService threadPool = Executors.newFixedThreadPool(Globals.threads);
    static LinkedList<NodeHashMap> solutions = new LinkedList<NodeHashMap>();
    static Visitor visitor = new Visitor(Globals.threads);
    //TODO remainder creation can be tweaked for performance
    //TODO folks on SO say to look at ConcurrentHashMap for performance.
    //TODO here's an idea -- you can split remainder into t hashtables, where t is the number of available threads. And then every thread can do marking/ etc in it
    
    NodeHashMap remainder;
    NodeHashMap scc;
    NodeHashMap predecessors;
    NodeHashMap descendants;
    
    public DataGraph(){
        remainder = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
        scc = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
        predecessors = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
        descendants = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
    }

    public DataGraph(NodeHashMap remainder){
        this.remainder = remainder;
        scc = new NodeHashMap(Globals.threads, Globals.sizeToCap(remainder.size()), Globals.loadFactor);
        predecessors = new NodeHashMap(Globals.threads, Globals.sizeToCap(remainder.size()), Globals.loadFactor);
        descendants = new NodeHashMap(Globals.threads, Globals.sizeToCap(remainder.size()), Globals.loadFactor);
    }
    
    public DataGraph(Iterable<Node> nodes){
        remainder = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
        for (Node node : nodes){
            remainder.add(node);
        }
    }
    
    /*public DataGraph(Node[] nodes){
        remainder = new NodeHashMap(Globals.threads, Globals.hashMapCap, Globals.loadFactor);
        for (Node node : nodes){
            remainder.add(node);
        }
    }*/
    
    public void addNode(Node node){
        node.reset(this);
        this.remainder.add(node);
    }

    /**
     * @param start -- this should be started immediately when threadPool spins a new thread. The job should be accessible from the stack if the stack is empty. The threadPool should wait for all threads to finish and once they do check for emptiness of stack. Or something like that. Thread safe. 
     */
    
}