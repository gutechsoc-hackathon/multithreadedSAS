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