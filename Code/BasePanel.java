import java.awt.*;
import javax.swing.*;

// <<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>
// CHANGING THIS PANEL WILL CHANGE THEM ALL
public class BasePanel extends JPanel{

    public BasePanel(String title,CardLayout cardlayout, JPanel cards)
    {
        setBackground(AppColors.background); // change the background color

        setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel label = new JLabel(title); // label for the page
        label.setForeground(AppColors.text); //Change the color of the text
        add(label);
    }
    
}
