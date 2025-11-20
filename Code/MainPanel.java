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
        TopBanner.setLayout(null); // This allows us to chose the percise location for each item
        TopBanner.setBounds(0,0,1535 ,200); // setBounds is needed for each item or they will not display on the page
        add(TopBanner);


        //Get the location for the image
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Resources/images/banner.png"));

        Image ScaledIcon = imageIcon.getImage().getScaledInstance(1535, 400,Image.SCALE_SMOOTH);
        
        ImageIcon ScaledImage = new ImageIcon(ScaledIcon);
        //Add an image to the top banner
        JLabel image = new JLabel(ScaledImage);
        image.setBounds(0,0,1535 ,100);
        TopBanner.add(image);


        //Create a section for the fan section or a container with some information
        JPanel FanSection = new JPanel();
        FanSection.setLayout(null); // used for absolute positioning
        FanSection.setBounds(40,250,300,200); //X - positon, Y - height, width - width of the section, height - height of the section
        FanSection.setBackground(Color.WHITE);
        FanSection.setBorder(BorderFactory.createLineBorder(Color.black,4,true));

        //The text to be added into the dashboard 
        JLabel Fan_Dashboard = new JLabel("Fan Dashboard");
        Fan_Dashboard.setForeground(Color.BLACK);
        Fan_Dashboard.setBounds(10,60,100,25);
        FanSection.add(Fan_Dashboard);
        //Add the text into the fan section/container
        add(FanSection);


        // Creating the fan button for the main panel
        JButton FanButton = new JButton("Continue as Fan");
        //Adds a border to the button
        FanButton.setBorder(BorderFactory.createLineBorder(AppColors.FanBorderColor, 2, true));
        //Add the fan button to the container storing the buttons horizontally in the page
        FanButton.setBounds(100, 400, 180, 35);

        //add the button to the main panel
        add(FanButton);
    }
}