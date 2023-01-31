import javax.swing.*;
import java.awt.*;
import java.util.Timer;


public class InputFrame extends JFrame {
    private static final int NUMB_OF_FIELD = 11;
    private static final Dimension FRAME_SIZE = new Dimension(350, 350);
    private final JTextField[] textField = new JTextField[NUMB_OF_FIELD];
    private final String[] stringTitle = {
            "Nombre de threads",
            "Frame rate",
            "Hauteur de la fenetre",
            "Largeur de la fenetre",
            "Population",
            "Angle de vue",
            "Distance de vue",
            "Rayon de braquage",
            "Taille de la zone de vue",
            "Force d'évaporation",
            "Force du blur"
    };
    private final String[] defaultValue = {
            "6",
            "60",
            "400",
            "400",
            "2000",
            "45",
            "9",
            "22",
            "1",
            "0.8",
            "0.08"
    };


    public InputFrame() {
        this.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(NUMB_OF_FIELD, 1, 3, 3));
        init(mainPanel);
        this.add(mainPanel, BorderLayout.CENTER);

        JButton validationButton = new JButton("Lancer la simulation");
        validationButton.addActionListener((event) -> validationAction());
        this.add(validationButton, BorderLayout.SOUTH);

        setSize(FRAME_SIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }


    private void init(JPanel mainPanel) {
        for (int i = 0; i < NUMB_OF_FIELD; i++) {
            JPanel panel = new JPanel(), textPanel = new JPanel();
            FlowLayout layout = new FlowLayout(FlowLayout.RIGHT);

            panel.setLayout(new GridLayout(1, 2));
            textPanel.setLayout(layout);

            textPanel.add(new JLabel(stringTitle[i]));
            textField[i] = new JTextField(defaultValue[i], 20);

            panel.add(textPanel);
            panel.add(textField[i]);
            mainPanel.add(panel);
        }
    }


    private void validationAction() {
        try {
            ApplicationParameters applicationParameters = new ApplicationParameters(
                    Integer.parseInt(textField[0].getText()),
                    Integer.parseInt(textField[1].getText()),
                    new Dimension(
                            Integer.parseInt(textField[2].getText()),
                            Integer.parseInt(textField[3].getText()))
                    );

            AgentParameters agentParameters = new AgentParameters(
                    Integer.parseInt(textField[4].getText()),
                    Integer.parseInt(textField[5].getText()),
                    Integer.parseInt(textField[6].getText()),
                    Integer.parseInt(textField[7].getText()),
                    Integer.parseInt(textField[8].getText())
                    );

            CanvasParameters canvasParameters = new CanvasParameters(
                    Float.parseFloat(textField[9].getText()),
                    Float.parseFloat(textField[10].getText())
            );

            startSimulation(applicationParameters, agentParameters, canvasParameters);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Saisie invalide", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void startSimulation(ApplicationParameters application, AgentParameters agent, CanvasParameters canvas) {
        this.dispose();
        Frame frame = new Frame(application, agent, canvas);
        Timer timer = new Timer(true);

        DrawTask drawTask = new DrawTask(frame.getCanvas());
        timer.scheduleAtFixedRate(drawTask, 0, 1000 / application.frameRate());
    }
}
