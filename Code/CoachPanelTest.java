import javax.swing.*;
import java.awt.*;

public class CoachPanelTest {
    public static void main(String[] args) {

        // Always start Swing on EDT
        SwingUtilities.invokeLater(() -> {

            // Create a window
            JFrame frame = new JFrame("CoachPanel Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 800);

            // Card layout + root card panel (required by BasePanel)
            CardLayout layout = new CardLayout();
            JPanel cards = new JPanel(layout);

            // Create CoachPanel
            CoachPanel coachPanel = new CoachPanel(layout, cards);

            // Add to cards so BasePanel works correctly
            cards.add(coachPanel, "COACH");

            // Show coach panel by default
            layout.show(cards, "COACH");

            // Add to frame
            frame.add(cards);

            frame.setLocationRelativeTo(null); // center on screen
            frame.setVisible(true);
        });
    }
}
