import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private final Canvas canvas;


    public Frame(ApplicationParameters application, AgentParameters agent, CanvasParameters canvasParameters) {
        super();
        Dimension size = new Dimension(application.frameSize());
        this.canvas = new Canvas(size, application, agent, canvasParameters);

        this.setSize(size);
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
