import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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


public class Coppersmith2005{
    public static void main(String[] args){
        DataGraph dg = GraphFactory.makeSanityCheckGraph();
        Runnable ur = new UltimateRecurssion(DataGraph.threadPool, DataGraph.visitor, dg);
        Future value = DataGraph.threadPool.submit(ur);
        synchronized (UltimateRecurssion.counter){
            //System.out.println("main conter: " + UltimateRecurssion.counter);

            while (UltimateRecurssion.counter.get() != 0){
                try {
                    UltimateRecurssion.counter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("finished main conter");
        }
        
        DataGraph.threadPool.shutdown();
        System.out.println("size " + UltimateRecurssion.solutions.size());
        for(NodeHashMap nhm: UltimateRecurssion.solutions){
            Node[] array = new Node[nhm.size()];
            nhm.toArray(array);
            for(Node node: array){
                System.out.println("node: " + node.id + " belongs to " + node.graph);
            }
        }
        System.out.println("finished");
        
    }
    /*public static void main(String[] args){
        DataGraph dg = GraphFactory.makeSanityCheckGraph();
        Node[] nodes = new Node[dg.remainder.size()];
        dg.remainder.toArray(nodes);
        for(Node node : nodes){
            System.out.println(node.id);
        }
    }*/
}



