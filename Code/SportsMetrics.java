import java.awt.*;
import javax.swing.*;



public class SportsMetrics {

    JFrame frame;
    JPanel MainPanel; // Main panel of the system
    JPanel fanPanel; // fan panel/ window
    JPanel coachPanel; // Coach panel/ window

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
        JLabel fanLabel = new JLabel("Fans Page");
        fanPanel.setBackground(AppColors.background);
        fanLabel.setForeground(AppColors.text); // Used to change the text white
        fanPanel.add(fanLabel);

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
