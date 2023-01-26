import java.lang.reflect.Array;

public class Utilities {
    public static <T> T[][] copyArray(Class<T> c, T[][] src) {
        @SuppressWarnings("unchecked")
        T[][] dst = (T[][]) Array.newInstance(c, src.length, src[0].length);

        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, src[i].length);
        }

        return dst;
    }

    public static int max3(int a, int b, int c) {
        int maxi = Math.max(a, b);

        if (maxi < c) {
            maxi = c;
        }

        return maxi;
    }

    public static int min3(int a, int b, int c) {
        int mini = Math.min(a, b);

        if (c < mini) {
            mini = c;
        }

        return mini;
    }
}
