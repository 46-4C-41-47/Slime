import java.lang.reflect.Array;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        Frame frame = new Frame();
        Timer timer = new Timer(true);

        DrawTask drawTask = new DrawTask(frame.getCanvas());
        timer.scheduleAtFixedRate(drawTask, 0, 1000 / Parameters.FRAME_RATE);
    }


    public static <T> T[][] copyArray(Class<T> c, T[][] src) {
        @SuppressWarnings("unchecked")
        T[][] dst = (T[][]) Array.newInstance(c, src.length, src[0].length);

        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, src[i].length);
        }

        return dst;
    }
}