package test;

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

import java.io.Serializable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataGraph implements Serializable{
    private static final long serialVersionUID = 1L;
    static ExecutorService threadPool = Executors.newFixedThreadPool(GraphFactory.locales.threads);
    static PriorityBlockingQueue<NodeHashMap> solutions = new PriorityBlockingQueue<NodeHashMap>();
    
    NodeHashMap remainder;
    NodeHashMap scc;
    NodeHashMap predecessors;
    NodeHashMap descendants;
    
    public DataGraph(){
        try{
            remainder = new NodeHashMap(GraphFactory.locales.threads, GraphFactory.locales.hashMapCap, GraphFactory.locales.loadFactor);
            scc = new NodeHashMap(GraphFactory.locales.threads, GraphFactory.locales.hashMapCap, GraphFactory.locales.loadFactor);
            predecessors = new NodeHashMap(GraphFactory.locales.threads, GraphFactory.locales.hashMapCap, GraphFactory.locales.loadFactor);
            descendants = new NodeHashMap(GraphFactory.locales.threads, GraphFactory.locales.hashMapCap, GraphFactory.locales.loadFactor);
        } catch (NullPointerException e){
            System.err.println("remember to set locales by invoking DataGraph.setLocales()");
        }
    }

    public DataGraph(NodeHashMap remainder){
        try{
            this.remainder = remainder;
            scc = new NodeHashMap(GraphFactory.locales.threads, GraphFactory.sizeToCap(remainder.size()), GraphFactory.locales.loadFactor);
            predecessors = new NodeHashMap(GraphFactory.locales.threads, GraphFactory.sizeToCap(remainder.size()), GraphFactory.locales.loadFactor);
            descendants = new NodeHashMap(GraphFactory.locales.threads, GraphFactory.sizeToCap(remainder.size()), GraphFactory.locales.loadFactor);
        } catch (NullPointerException e){
            System.err.println("remember to set locales by invoking DataGraph.setLocales()");
        }
    }
    
    public DataGraph(Iterable<Node> nodes){
        try {
            remainder = new NodeHashMap(GraphFactory.locales.threads, GraphFactory.locales.hashMapCap, GraphFactory.locales.loadFactor);
            for (Node node : nodes){
                remainder.add(node);
            }
        } catch (NullPointerException e){
            System.err.println("remember to set locales by invoking DataGraph.setLocales()");
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
    
}