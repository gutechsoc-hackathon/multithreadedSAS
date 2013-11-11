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

//TODO I don't like this class anymore, it should be refactored into a factory.
final class Globals{

    public static final int problemSizeReal = 100000;
    public static final int problemSizeTests = 8567;
    public static final int threads = 2;
    public static final int childrenThreshold = 5;
    public static final int hashMapCap = sizeToCap(problemSizeReal);
    public static final float loadFactor = (float) 0.75;
    public static int sizeToCap(int size){
        return (int) (problemSizeReal/(loadFactor - 0.01)) + 7;
    }

}