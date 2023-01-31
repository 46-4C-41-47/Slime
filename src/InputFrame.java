import javax.swing.*;
import java.awt.*;

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
            "12",
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

        } catch (NumberFormatException e) {
            new JOptionPane("Saisie invalide", JOptionPane.ERROR_MESSAGE);
        }
    }
}
