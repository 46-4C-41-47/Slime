import java.lang.reflect.Array;

public class Main {
    public static void main(String[] args) {
        new InputFrame();
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