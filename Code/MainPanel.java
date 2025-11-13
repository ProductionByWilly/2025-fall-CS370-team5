import java.awt.*;
import javax.swing.*;

public class MainPanel extends BasePanel {

    public MainPanel(CardLayout cardLayout,JPanel cards)
    {
        // Super calls the constructor from the parent
        //In this case it will call the constructore for the base panel and use it 
        super("Main Page",cardLayout,cards);
<<<<<<< HEAD

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
=======
        //Create the different buttons for each category
        JButton fanButton = new JButton("Fans");
        JButton CoachButton = new JButton("Coach");
        JButton PlayerButton = new JButton("Players");

        add(fanButton);
        add(CoachButton);
        add(PlayerButton);

        // -- Subpanel for username label and text entry fields --
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        usernamePanel.setBackground(AppColors.background);
        JLabel userLabel = new JLabel("Please enter your username: ");
        userLabel.setForeground(AppColors.text); // Used to change the text white
        JTextField userText = new JTextField(15);
        userText.setMaximumSize(new Dimension(200, 25));

        usernamePanel.add(userLabel);
        usernamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        usernamePanel.add(userText);
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(usernamePanel);

        // -- subpanel for Password label and text entry fields --
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.setBackground(AppColors.background);
        JLabel passLabel = new JLabel("Please enter your Password: ");
        passLabel.setForeground(AppColors.text); // Used to change the text white
        JTextField passText = new JTextField(15);
        passText.setMaximumSize(new Dimension(200, 25));

        passwordPanel.add(passLabel);
        passwordPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        passwordPanel.add(passText);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(passwordPanel);

        // ------- To show the corresponding page the name must match with that name -----
        // Clicking the fan page must match the same label that panel has
        fanButton.addActionListener(e -> cardLayout.show(cards,"Fan Page"));
        CoachButton.addActionListener(e -> cardLayout.show(cards,"Coach Page"));
        PlayerButton.addActionListener(e -> cardLayout.show(cards,"Player Page"));
>>>>>>> a355138078814aabc832f5cdcf49c215fbf1f2f9
    }
}