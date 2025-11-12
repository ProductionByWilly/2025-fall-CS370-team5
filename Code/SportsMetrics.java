import java.awt.*;
import javax.swing.*;

public class SportsMetrics {

    JFrame frame;
    //Used to hold the different panels in the system
    JPanel cards;
    CardLayout cardLayout;


    public void CreateGUI()
    {
        frame = new JFrame("CSU Sports Metrics"); // Create a new frame window frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Once the close is clicked end the program
        frame.setLayout(new BorderLayout());  // set the layout


        //Create the different buttons for each category
        JButton fanButton  = new JButton("Fans");
        JButton CoachButton = new JButton("Coach");
        JButton PlayerButton = new JButton("Players");


        // ------- Main Panel System Display  -------
        MainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Used to move the buttons around the frame
        MainPanel.setBackground(AppColors.background); // Access the AppColors to access different colors for the different sections
        
        // ----- Add the buttons to the main panel -----
        MainPanel.add(fanButton);
        MainPanel.add(CoachButton);
        MainPanel.add(PlayerButton);
        

        // ------- Fan Panel System Display  -------
        fanPanel = new JPanel();
        fanPanel.setLayout(new BoxLayout(fanPanel, BoxLayout.Y_AXIS));
        fanPanel.setBackground(AppColors.background);

        // -- Title Label --
        JLabel fanLabel = new JLabel("Fans Page");
        fanLabel.setForeground(AppColors.text); // Used to change the text white
        fanLabel.setAlignmentX(Component.CENTER_ALIGNMENT); //centers the text horizontal
        fanPanel.add(fanLabel);

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

        fanPanel.add(usernamePanel);

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

        fanPanel.add(passwordPanel);


        // ------- Coach Panel System Display  -------
        coachPanel = new JPanel();
        JLabel coachLabel = new JLabel("Coach Page");
        coachPanel.setBackground(AppColors.background);
        coachLabel.setForeground(AppColors.text); // Used to change the text white
        coachPanel.add(coachLabel);

        // ------- Player Panel System Display  -------
        JPanel playerPanel = new JPanel();
        JLabel playerLabel = new JLabel("Player Page");
        playerPanel.setBackground(AppColors.background);
        playerLabel.setForeground(AppColors.text); // Used to change the text white
        playerPanel.add(playerLabel);

        //Card layout to hold the different panels we are using
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // ------ Add the different panels into the card with their corresponding names -----
        cards.add(MainPanel,"Home Page");
        cards.add(fanPanel,"Fan Page");
        cards.add(coachPanel,"Coach Page");
        cards.add(playerPanel,"Player Page");

        // ------- To show the corresponding page the name must match with that name -----
        // Clicking the fan page must match the same label that panel has
        fanButton.addActionListener(e -> cardLayout.show(cards,"Fan Page"));
        CoachButton.addActionListener(e -> cardLayout.show(cards,"Coach Page"));
        PlayerButton.addActionListener(e -> cardLayout.show(cards,"Player Page"));

        frame.add(cards,BorderLayout.CENTER);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);  //Once the application is opened maximize the window
        frame.setVisible(true); // makes the window visible to the user
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> new SportsMetrics().CreateGUI());
    }
}

