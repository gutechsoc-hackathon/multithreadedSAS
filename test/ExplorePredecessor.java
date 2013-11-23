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

    class ExplorePredecessors extends GraphExplorator{
        ExplorePredecessors(Node start, DataGraph dg){
            super(start, dg);
        }
        @Override
        public void run(){
            Node current = start;
            while(current.mark_predecessor(dg)){
                for(int i = 1; i < current.parents.size(); i++){
                    counter.incrementAndGet();
                    //if(executor.isShutdown()) throw new RuntimeException("shutdown!");
                    executor.execute(new ExplorePredecessors(current.parents.get(i), dg));
                }
                if (current.parents.size() > 0){
                    System.out.println("current node is " + current.id);
                    current = current.parents.get(0);
                }
            }
            counter.decrementAndGet();
            synchronized (counter) {
                counter.notifyAll();
            }
        }
    }