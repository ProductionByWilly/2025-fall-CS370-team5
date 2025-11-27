import java.awt.*;
import javax.swing.*;

public class PollsPanel extends BasePanel {
    public PollsPanel(CardLayout cardLayout, JPanel cards) {
        super("Polls Page", cardLayout, cards);

        // Page layout setup
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppColors.darkTeal);

        add(Box.createRigidArea(new Dimension(0, 20)));

        // Page title
        JLabel title = new JLabel("Fan Polls");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(Color.WHITE);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 10)));

        // Back button to return to Fan dashboard
        JButton backButton = new JButton("Back to Fan Dashboard");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cardLayout.show(cards, "Fan Page"));
        add(backButton);

        add(Box.createVerticalGlue());
    }
}
