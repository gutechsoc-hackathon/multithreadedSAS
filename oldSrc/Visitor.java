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

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;


public class Visitor{
    
    class VisitorThread extends Thread{

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
            finishedCounter.countDown();
        }
    }

    final int threadsNumber;
    DataGraph dg;
    VisitorThread[] threads;
    CountDownLatch finishedCounter;
    
    public Visitor(int threadsNumber){
        threads = new VisitorThread[threadsNumber];
        this.threadsNumber = threadsNumber;
        finishedCounter = new CountDownLatch(threadsNumber);
    }
    
    public void setDG(DataGraph dg){
        this.dg = dg;
    }

    public Visitor(int threadsNumber, DataGraph dg){
        this.dg = dg;
        threads = new VisitorThread[threadsNumber];
        this.threadsNumber = threadsNumber;
        finishedCounter = new CountDownLatch(threadsNumber);
    }
    
    public void newJob(NodeHashMap nhm){
        assert(nhm.threadsNumber == this.threadsNumber);
        int i = 0;
        for(ConcurrentHashMap<Long, Node> map: nhm.maps){
            threads[i] = new VisitorThread(map, dg);
            i++;
        }
    }
    
    public void visitAll(){
        for(Thread thread : threads){
        	System.out.println("here");
            thread.start();
        }

    }
    

}

