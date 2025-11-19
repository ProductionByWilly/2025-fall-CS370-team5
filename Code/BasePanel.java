import java.awt.*;
import javax.swing.*;

// <<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>
// CHANGING THIS PANEL WILL CHANGE THEM ALL
public class BasePanel extends JPanel{
    protected CardLayout cardLayout;
    protected JPanel cards;

    public BasePanel(String title,CardLayout cardlayout, JPanel cards)
    {
        this.cardLayout = cardlayout;
        this.cards = cards;
        setBackground(AppColors.darkTeal); // change the background color
        setLayout(null);

        JLabel label = new JLabel(title); // label for the page
        label.setForeground(Color.white); //Change the color of the text
        add(label);
    }
    
}
