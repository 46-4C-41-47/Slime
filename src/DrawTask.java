import java.util.TimerTask;

public class DrawTask extends TimerTask {
    private Canvas canvas;
    private Time time;
    private int loopCount = 0;
    private double avgFrameRate = 0d;

    public DrawTask(Canvas c) {
        canvas = c;
        time = new Time();
    }


    @Override
    public void run() {
        time.start();

        canvas.draw();
        canvas.paintComponent(canvas.getGraphics());

        if (loopCount == 60 * 10) {
            //System.out.println((int) (avgFrameRate / (60 * 10)));
            loopCount = -1;
            avgFrameRate = 0;
            //System.exit(0);
        }
        avgFrameRate += time.getFrameRate();

        loopCount++;
    }
}
