import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Frame extends JFrame {
    private Dimension size;
    private Canvas canvas;

    public Frame() {
        super();
        this.size = new Dimension(Parameters.FRAME_SIZE);
        this.canvas = new Canvas(this.size);

        this.setSize(this.size);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(canvas);
        this.setVisible(true);
    }


    public Canvas getCanvas() {
        return canvas;
    }
}
