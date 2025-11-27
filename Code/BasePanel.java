import java.awt.*;
import javax.swing.*;

// <<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>
// CHANGING THIS PANEL WILL CHANGE THEM ALL
public class BasePanel extends JPanel {
    protected CardLayout cardLayout;
    protected JPanel cards;

    public BasePanel(String title, CardLayout cardlayout, JPanel cards) {
        this.cardLayout = cardlayout;
        this.cards = cards;
        setBackground(AppColors.darkTeal);
    }
}