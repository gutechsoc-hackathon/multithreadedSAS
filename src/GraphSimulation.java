package test;

import java.rmi.Remote;



public interface GraphSimulation extends Remote{
    public DataGraph getDataGraph();
    public int numberOfSolutions();
    public int getSolutionSize(int index);
    //public ParHashMap getSolution();
    //public int getLargestSolution();
}
