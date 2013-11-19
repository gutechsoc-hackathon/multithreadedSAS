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

import java.util.ArrayList;
import java.util.Random;

class Node/* implements Comparable<Node>*/{
	final long id;
	final static int childrenNo = 5;
	boolean predecessor;
	boolean descendant;
	DataGraph graph;
	ArrayList<Node> children = new ArrayList<Node>(childrenNo);
	ArrayList<Node> parents = new ArrayList<Node>(childrenNo);
	
	/**
	 * @param id id == 0 will break your code. You have been warned.
	 */
    public Node(long id){
        this.id = id;
        predecessor = false;
    	descendant = false;
    	graph = null;

    }
    
    /**
     * This isn't thread safe, use it only once, when you prepare the graph.
     */
    public void connectChild(Node child){
        child.parents.add(this);
        this.children.add(child);
    }
    
    public synchronized void reset(DataGraph graph){
        this.graph = graph;
        predecessor = false;
        descendant = false;
        notifyAll();
        return;
    }
    
    //TODO this can be tweaked in many ways. There's some redundancy. Also, I'd like to see it merged with mark_descendants. I'm afraid to touch it at the moment, since my testcases aren't very powerful, and there's nothing simpler than poison your code with race conditions.
    public synchronized boolean mark_predecessor(DataGraph graph){
        if(predecessor == false && graph == this.graph){
            predecessor = true;
            if(descendant == true){
                graph.scc.add(this);
                graph.descendants.remove(this);
            } else{
                graph.predecessors.add(this);
                graph.remainder.remove(this);
            }
            notifyAll();
            //System.out.println("mark_pred finished: " + value);
            return true;
        } else{
            notifyAll();
            //System.out.println("mark_pred finished: " + value);
            return false;
        }

    }
    
    public synchronized boolean mark_descendant(DataGraph graph){
        System.out.println("node enter desc");
        if(descendant == false && graph == this.graph){
            descendant = true;
            if(predecessor == true){
                graph.scc.add(this);
                graph.predecessors.remove(this);
            } else{
                graph.descendants.add(this);            
                graph.remainder.remove(this);
            }
            notifyAll();
            return true;
        } else{
            notifyAll();
            return false;
        }
    }
    
    /*@Override
    public int compareTo(Node node) {
        return Long.compare(this.id, node.id);
    }*/
}
