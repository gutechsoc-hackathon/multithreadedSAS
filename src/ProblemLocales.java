package test;

class ProblemLocales {
    public int hashMapCap;
    public int threads;
    public float loadFactor = (float) 0.75;

    public ProblemLocales(int hashMapCapacity, int numberOfThreads, float hashMapLoadFactor){    
        hashMapCap = hashMapCapacity;
        threads = numberOfThreads;
        loadFactor = hashMapLoadFactor;
    }
}
