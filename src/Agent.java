import java.awt.*;

public class Agent {
    private final double speed = 1;
    private final Dimension scope;
    private double x, y;
    private Vector2D vector;


    public Agent(Dimension scope) {
        this.vector = new Vector2D(Math.random() * 360, this.speed);
        this.scope = scope;
        //spawning coordinates
        //this.x = scope.width * Math.random();
        //this.y = scope.height * Math.random();
        //this.x = ((scope.height  / 2) * Math.random()) + scope.width  / 4;
        //this.y = ((scope.height  / 2) * Math.random()) + scope.height  / 4;
        //this.x = scope.width / 2f;
        //this.y = scope.height / 2f;
        initSpawn();
    }


    private void initSpawn() {
        int resolution = 20, rayon = scope.height / 2;
        float angle = 360 / (float) resolution;
        int[] x = new int[resolution];
        int[] y = new int[resolution];
        int centerX = scope.width / 2;
        int centerY = scope.height / 2;

        for (int i = 0; i < resolution; i++) {
            x[i] = (int) (Math.cos(Math.toRadians(angle) * i) * rayon) + centerX;
            y[i] = (int) (Math.sin(Math.toRadians(angle) * i) * rayon) + centerY;
        }

        Polygon circle = new Polygon(x, y, resolution);

        do {
            this.x = scope.width * Math.random();
            this.y = scope.height * Math.random();

        } while (!circle.contains(this.x, this.y));
    }


    private void outOfBounds(double x, double y) {
        if (x < 0) {
            double alpha = Math.random() * 180;

            if (90 < alpha) {
                alpha += 90;
            }
            this.vector = vector.rotate(alpha);
            return;
        } else if (scope.width <= x){
            this.vector = vector.rotate(Math.random() * 180 + 90);
            return;
        }
        
        if (y < 0) {
            this.vector = vector.rotate(Math.random() * 180 + 180);
            return;
        } else if (scope.height < y) {
            this.vector = vector.rotate(Math.random() * 180);
            return;
        }

        /*if ((x < 0 && y < 0) || (x < 0 && scope.height <= y) || (scope.width <= x && y < 0) || (scope.width <= x && scope.height <= y)) {
            this.x = scope.width / 2f;
            this.y = scope.height / 2f;
        }*/
    }


    public void move() {
        double newX = this.x + vector.getX(), newY = this.y + vector.getY();

        if (0 <= newX && newX < scope.width && 0 <= newY && newY < scope.height) {
            this.x = newX;
            this.y = newY;

        } else {
            outOfBounds(newX, newY);
        }
    }


    public void rotate(int alpha) {
        this.vector = this.vector.rotate(alpha);
    }


    public int getX() {
        return (int) x;
    }


    public int getY() {
        return (int) y;
    }


    public double getAngle() {
        return this.vector.getTheta();
    }


    public Vector2D getVector() {
        return this.vector;
    }
}
