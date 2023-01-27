public class Vector2D {
    private double x, y, length, theta;


    public Vector2D() {
        this.x      = 1;
        this.y      = 0;
        this.length = computeLength();
        this.theta  = 0;
    }

    public Vector2D(double alpha) {
        this.x      = computeX(invertAlpha(alpha));
        this.y      = computeY(invertAlpha(alpha));
        this.length = 1;
        this.theta  = alpha;
    }

    public Vector2D(double alpha, double length) {
        this.length = length;
        this.x      = computeX(invertAlpha(alpha)) * length;
        this.y      = computeY(invertAlpha(alpha)) * length;
        this.theta  = alpha;
    }


    /*public Vector2D(int x1, int y1, int x2, int y2) {
        this.x      = x2 - x1;
        this.y      = y2 - y1;
        setLength(1);
        this.theta  = 0;
    }*/


    public Vector2D rotate(double alpha) {
        return new Vector2D(theta + alpha, length);
    }

    private double invertAlpha(double alpha) {
        return 360 - alpha;
    }


    private double computeX(double alpha) {
        return Math.cos(Math.toRadians(alpha));
    }

    private double computeY(double alpha) {
        return Math.sin(Math.toRadians(alpha));
    }

    private double computeLength() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }


    public void setX(double x) {
        this.x = x;
        this.length = computeLength();
    }

    public void setY(double y) {
        this.y = y;
        this.length = computeLength();
    }

    public void setLength(double length) {
        x = x * length / this.length;
        y = y * length / this.length;
        this.length = length;
    }


    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getLength() {
        return length;
    }

    public double getTheta() {
        return this.theta % 360;
    }

    public Vector2D getTrigonometricVector() {
        return new Vector2D(this.x / this.length, this.y /this.length);
    }
}
