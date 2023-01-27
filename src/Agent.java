import java.awt.*;

public class Agent {
    private final double speed = 1;
    private final Dimension scope;
    private double x, y;
    private Vector2D vector;


    public Agent(Dimension scope) {
        this.scope = scope;
        vector = new Vector2D(Math.random() * 360, speed);

        x = scope.width / 2f;
        y = scope.height / 2f;
    }


    public Agent(Dimension scope, Polygon spawningArea) {
        this.scope = scope;
        vector = new Vector2D(Math.random() * 360, speed);
        spawn(spawningArea);
    }


    private void spawn(Polygon spawningArea) {
        do {
            this.x = scope.width * Math.random();
            this.y = scope.height * Math.random();

        } while (!spawningArea.contains(this.x, this.y));
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
