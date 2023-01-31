import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private Dimension size;
    private Canvas canvas;

    public Frame(ApplicationParameters application, AgentParameters agent, CanvasParameters canvasParameters) {
        super();
        this.size = new Dimension(application.frameSize());
        this.canvas = new Canvas(this.size, application, agent, canvasParameters);

        this.setSize(this.size);
        this.add(canvas);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }


    public Canvas getCanvas() {
        return canvas;
    }
}
