public class Time {
    private long startingTime;

    public void start() {
        startingTime = System.currentTimeMillis();
    }

    public long getRunTime() {
        return System.currentTimeMillis() - startingTime;
    }

    public double getFrameRate() throws NullPointerException {
        long runTime = System.currentTimeMillis() - startingTime;
        return 1000 / (double) runTime;
    }
}
