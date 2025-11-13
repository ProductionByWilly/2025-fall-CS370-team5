import java.awt.*;
import javax.swing.*;

public class MainPanel extends BasePanel {

    public MainPanel(CardLayout cardLayout,JPanel cards)
    {
        // Super calls the constructor from the parent
        //In this case it will call the constructore for the base panel and use it 
        super("Main Page",cardLayout,cards);

        //Create a section for the banner on the top
        JPanel TopBanner = new JPanel();
        TopBanner.setLayout(null);
        TopBanner.setBounds(0,0,1535 ,130);
        TopBanner.setBackground(Color.cyan);
        add(TopBanner);


        //Add an image to the top banner
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Resources/images/banner.jpg"));
        JLabel image = new JLabel(imageIcon);
        image.setBounds(0,0,1535 ,130);
        TopBanner.add(image);


        JPanel FanSection = new JPanel();
        FanSection.setLayout(null); // used for absolute positioning
        FanSection.setBounds(40,250,300,200); //X - positon, Y - height, width - width of the section, height - height of the section
        FanSection.setBackground(Color.WHITE);
        FanSection.setBorder(BorderFactory.createLineBorder(Color.black,4,true));
        JLabel Fan_Dashboard = new JLabel("Fan Dashboard");
        Fan_Dashboard.setForeground(Color.BLACK);
        Fan_Dashboard.setBounds(10,60,100,25);
        FanSection.add(Fan_Dashboard);
        add(FanSection);


        // Creating the fan button for the main panel
        JButton FanButton = new JButton("Continue as Fan");
        //Adds a border to the button
        FanButton.setBorder(BorderFactory.createLineBorder(AppColors.FanBorderColor, 2, true));
        //Add the fan button to the container storing the buttons horizontally in the page
        FanButton.setBounds(100, 400, 180, 35);
        add(FanButton);
    }
}