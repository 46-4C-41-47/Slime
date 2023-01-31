import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Canvas extends JPanel {
    private final ExecutorService executor;
    private final CanvasParameters canvasParameters;
    private final AgentParameters agentParameters;
    private final BufferedImage screen;
    private final Dimension size;
    private Float[][] trailMap;
    private Agent[] agents;


    public Canvas(Dimension d, ApplicationParameters applicationParameters, AgentParameters agentParameters, CanvasParameters canvasParameters) {
        super();
        this.agentParameters = agentParameters;
        this.canvasParameters = canvasParameters;
        executor = Executors.newFixedThreadPool(applicationParameters.numbOfThreads());
        size = new Dimension(d.width - 16, d.height - 39);
        this.screen = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D screenGraphics = (Graphics2D) this.screen.getGraphics();
        screenGraphics.setColor(Color.BLACK);
        screenGraphics.fillRect(0, 0, size.width, size.height);

        init();
    }


    private void init() {
        agents = new Agent[agentParameters.population()];
        trailMap = new Float[size.width][size.height];
        Polygon spawningArea = createSpawningArea();

        for (int i = 0; i < agents.length; i++) agents[i] = new Agent(size, spawningArea);

        for (Float[] floats : trailMap) Arrays.fill(floats, 0f);
    }


    private Polygon createSpawningArea() {
        int resolution = 20, rayon = screen.getHeight() / 4;
        float angle = 360 / (float) resolution;
        int[] x = new int[resolution];
        int[] y = new int[resolution];
        int centerX = screen.getWidth() / 2;
        int centerY = screen.getHeight() / 2;

        for (int i = 0; i < resolution; i++) {
            x[i] = (int) (Math.cos(Math.toRadians(angle) * i) * rayon) + centerX;
            y[i] = (int) (Math.sin(Math.toRadians(angle) * i) * rayon) + centerY;
        }

        return new Polygon(x, y, resolution);
    }


    public void draw() {
        Time totalTime = new Time();
        totalTime.start();
        Color c;
        Float[][] prevTrailMap = Main.copyArray(Float.class, trailMap);
        int RGB;

        Future<Integer> finishingState = executor.submit(this::processTrails);

        //System.out.print("Move agent : " + moveAgent() + " ms, ");
        moveAgent();

        try {
            finishingState.get();
        } catch (Exception e) {
            System.out.println("Error");
        }

        //Time repaintTime = new Time();

        for (int i = 0; i < screen.getWidth(); i++) {
            for (int j = 0; j < screen.getHeight(); j++) {
                if (trailMap[i][j].doubleValue() != prevTrailMap[i][j].doubleValue()) {
                    RGB = Math.round(this.trailMap[i][j]);
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
        float sum, aroundConcentration, temp;
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

                aroundConcentration = sum / 9;
                temp = this.trailMap[i][j] - (this.trailMap[i][j] - aroundConcentration) * canvasParameters.blurWeight();

                temp -= canvasParameters.evaporationWeight();

                if (0 < temp) {
                    this.trailMap[i][j] = temp;
                } else {
                    this.trailMap[i][j] = 0f;
                }
            }
        }

        //System.out.print("Process trail : " + (int) time.getRunTime() + " ms, ");
        return 1;
    }


    private float sense(Agent agent, double sensorAngleOffset) {
        double sensorAngle = agent.getAngle() + sensorAngleOffset;
        Vector2D sensorVector = new Vector2D(sensorAngle, agentParameters.viewDistance());
        float sensorX = (float) (agent.getX() + sensorVector.getX());
        float sensorY = (float) (agent.getY() + sensorVector.getY());
        float sum = 0f;

        for (int i = -agentParameters.sensorSize(); i < agentParameters.sensorSize(); i++) {
            for (int j = -agentParameters.sensorSize(); j < agentParameters.sensorSize(); j++) {
                sensorX += i;
                sensorY += j;

                if (0 <= sensorX && sensorX < size.width && 0 <= sensorY && sensorY < size.height) {
                    sum += this.trailMap[(int) sensorX][(int) sensorY];
                }
            }
        }

        return sum;
    }


    private float[] getConcentration(Agent agent) {
        return new float[]{
                sense(agent, -agentParameters.sensorAngle()),
                sense(agent, 0),
                sense(agent, agentParameters.sensorAngle())
        };
    }


    private int moveAgent() {
        Time time = new Time();
        time.start();
        Future<float[]>[] concentrations = new Future[this.agents.length];

        for (int i = 0; i < agents.length; i++) {
            final int tempIndex = i;
            concentrations[i] = executor.submit(() -> getConcentration(agents[tempIndex]));
        }

        for (int i = 0; i < agents.length; i++) {
            float[] concentration = {0, 0, 0};

            trailMap[agents[i].getX()][agents[i].getY()] = 255f;

            try {
                concentration = concentrations[i].get();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            rotateAgent(concentration, agents[i]);

            agents[i].move();
        }

        return (int) time.getRunTime();
    }


    private void rotateAgent(float[] concentration, Agent agent) {
        if (concentration[1] < concentration[0] && concentration[1] < concentration[2]) {
            if (Math.random() < 0.5) {
                agent.rotate(agentParameters.turnRange());
            } else {
                agent.rotate(-agentParameters.turnRange());
            }

        } else if (concentration[0] < concentration[2]) {
            agent.rotate(agentParameters.turnRange());

        } else if (concentration[2] < concentration[0]) {
            agent.rotate(-agentParameters.turnRange());
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(this.screen, 0, 0, null);
        this.repaint();
    }
}
