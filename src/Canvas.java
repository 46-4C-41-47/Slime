import javax.swing.JPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Canvas extends JPanel {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Parameters.NUMB_OF_THREADS);
    private final BufferedImage screen;
    private final Dimension size;
    private Double[][] trailMap;
    private Agent[] agents;


    public Canvas(Dimension d) {
        super();
        size = new Dimension(d.width - 16, d.height - 39);
        this.screen = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D screenGraphics = (Graphics2D) this.screen.getGraphics();
        screenGraphics.setColor(Color.BLACK);
        screenGraphics.fillRect(0, 0, size.width, size.height);

        init();
    }


    private void init() {
        agents = new Agent[Parameters.POPULATION];
        trailMap = new Double[size.width][size.height];

        for (int i = 0; i < agents.length; i++) agents[i] = new Agent(size);

        for (Double[] doubles : trailMap) Arrays.fill(doubles, 0d);
    }


    public void draw() {
        Time totalTime = new Time();
        totalTime.start();
        Color c;
        Double[][] prevTrailMap = Utilities.copyArray(Double.class, trailMap);
        int RGB;

        Future<Integer> finishingState = EXECUTOR.submit(this::processTrails);

        //System.out.print("Move agent : " + moveAgent() + " ms, ");
        moveAgent();

        try {
            finishingState.get();
        } catch (Exception e) {
            System.out.println("Error");
        }

        Time repaintTime = new Time();

        for (int i = 0; i < screen.getWidth(); i++) {
            for (int j = 0; j < screen.getHeight(); j++) {
                if (trailMap[i][j].doubleValue() != prevTrailMap[i][j].doubleValue()) {
                    RGB = (int) this.trailMap[i][j].doubleValue();
                    c = new Color(RGB, RGB, RGB);
                    screen.setRGB(i, j, c.getRGB());
                }
            }
        }

        //System.out.print("Repaint screen : " + repaintTime.getRunTime() + " ms, ");
        //System.out.println("Total : " + totalTime.getRunTime() + " ms");
    }


    private int processTrails() {
        Time time = new Time();
        time.start();
        double sum, blurValue, temp;
        int x, y;

        for (int i = 0; i < this.screen.getWidth(); i++) {
            for (int j = 0; j < this.screen.getHeight(); j++) {
                sum = 0;

                for (int offsetX = -1; offsetX <= 1; offsetX++) {
                    for (int offsetY = -1; offsetY <= 1; offsetY++) {
                        x = i + offsetX;
                        y = j + offsetY;

                        if (0 <= x && 0 <= y && x < this.screen.getWidth() && y < this.screen.getHeight()) {
                            sum += this.trailMap[x][y];
                        }
                    }
                }

                blurValue = sum / 9;
                temp = this.trailMap[i][j] - (this.trailMap[i][j] - blurValue) * Parameters.BLUR_WEIGHT;

                temp -= Parameters.EVAPORATION_WEIGHT;

                if (0 < temp) {
                    this.trailMap[i][j] = temp;
                } else {
                    this.trailMap[i][j] = 0d;
                }
            }
        }

        //System.out.print("Process trail : " + (int) time.getRunTime() + " ms, ");
        return 1;
    }


    private double sense(Agent agent, double sensorAngleOffset) {
        double sensorAngle = agent.getAngle() + sensorAngleOffset;
        Vector2D sensorVector = new Vector2D(sensorAngle, Parameters.VIEW_DISTANCE);
        double sensorX = agent.getX() + sensorVector.getX();
        double sensorY = agent.getY() + sensorVector.getY();
        double sum = 0d;

        for (int i = -Parameters.SENSOR_SIZE; i < Parameters.SENSOR_SIZE; i++) {
            for (int j = -Parameters.SENSOR_SIZE; j < Parameters.SENSOR_SIZE; j++) {
                sensorX += i;
                sensorY += j;

                if (0 <= sensorX && sensorX < size.width && 0 <= sensorY && sensorY < size.height) {
                    sum += this.trailMap[(int) sensorX][(int) sensorY];
                }
            }
        }

        return sum;
    }


    private double[] getConcentration(Agent agent) {
        return new double[]{
                sense(agent, -Parameters.SENSOR_ANGLE),
                sense(agent, 0),
                sense(agent, Parameters.SENSOR_ANGLE)
        };
    }


    private int moveAgent() {
        Time time = new Time();
        time.start();
        Future<double[]>[] concentrations = new Future[this.agents.length];

        for (int i = 0; i < agents.length; i++) {
            final int tempIndex = i;
            concentrations[i] = EXECUTOR.submit(() -> getConcentration(agents[tempIndex]));
        }

        for (int i = 0; i < agents.length; i++) {
            double[] concentration = {0, 0, 0};

            this.trailMap[agents[i].getX()][agents[i].getY()] = 255d;

            try {
                concentration = concentrations[i].get();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            if (concentration[1] < concentration[0] && concentration[1] < concentration[2]) {
                if (Math.random() < 0.5) {
                    this.agents[i].rotate(Parameters.TURN_RANGE);
                } else {
                    this.agents[i].rotate(-Parameters.TURN_RANGE);
                }

            } else if (concentration[0] < concentration[2]) {
                this.agents[i].rotate(Parameters.TURN_RANGE);

            } else if (concentration[2] < concentration[0]) {
                this.agents[i].rotate(-Parameters.TURN_RANGE);
            }

            agents[i].move();
        }

        return (int) time.getRunTime();
    }


    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.screen, 0, 0, null);
        this.repaint();
    }
}
