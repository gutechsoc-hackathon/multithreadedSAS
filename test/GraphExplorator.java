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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;


abstract class GraphExplorator implements Runnable{
    static AtomicInteger counter = new AtomicInteger(1);
    final static ExecutorService executor;
    final Node start;
    final DataGraph dg;
    
    public static void setExecutor(ExecutorService executor){
        GraphExplorator.executor = executor;
    }
    
    GraphExplorator(Node start, DataGraph dg){
        this.start = start;
        this.dg = dg;
    }
        
    GraphExplorator(ExecutorService executor, Node start, DataGraph dg){
        this(start, dg);
        setExecutor(executor);        
    }
    
    @Override
    abstract public void run();
    
}