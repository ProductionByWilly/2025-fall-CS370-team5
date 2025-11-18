import java.awt.*;
import javax.swing.*;

public class SportsMetrics {

    private static final String HOME_CARD = "Home Page";
    private static final String FAN_LOGIN_CARD = "Fan Page";
    private static final String FAN_DASHBOARD_CARD = "Fan Dashboard";
    private static final String COACH_CARD = "Coach Page";
    private static final String PLAYER_CARD = "Player Page";

    JFrame frame;
    //Used to hold the different panels in the system
    JPanel cards;
    JPanel MainPanel;
    JPanel fanPanel;
    JPanel coachPanel;
    JPanel playerPanel;
    FanDashboardPanel fanDashboardPanel;
    CardLayout cardLayout;


    public void CreateGUI()
    {
        frame = new JFrame("CSU Sports Metrics"); // Create a new frame window frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Once the close is clicked end the program
        frame.setLayout(new BorderLayout());  // set the layout

        //Card layout to hold the different panels we are using
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // ------ Add the different panels into the card with their corresponding names -----
        cards.add(new MainPanel(cardLayout, cards) , "Home Page");

        // ------- Fan Panel System Display  -------
        cards.add(new FanPanel(cardLayout,cards),"Fan Page");

        // ------- Coach Panel System Display  -------
        cards.add(new CoachPanel(cardLayout,cards),"Coach Page");

        // ------- Player Panel System Display  -------
        playerPanel = new JPanel();
        JLabel playerLabel = new JLabel("Player Page");
        playerPanel.setBackground(AppColors.background);
        playerLabel.setForeground(AppColors.text); // Used to change the text white
        playerPanel.add(playerLabel);

        //Card layout to hold the different panels we are using
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // ------ Add the different panels into the card with their corresponding names -----
        fanDashboardPanel = new FanDashboardPanel();

        cards.add(MainPanel,HOME_CARD);
        cards.add(fanPanel,FAN_LOGIN_CARD);
        cards.add(coachPanel,COACH_CARD);
        cards.add(playerPanel,PLAYER_CARD);
        cards.add(fanDashboardPanel,FAN_DASHBOARD_CARD);

        // ------- To show the corresponding page the name must match with that name -----
        // Clicking the fan page must match the same label that panel has
        fanButton.addActionListener(e -> cardLayout.show(cards,FAN_LOGIN_CARD));
        CoachButton.addActionListener(e -> cardLayout.show(cards,COACH_CARD));
        PlayerButton.addActionListener(e -> cardLayout.show(cards,PLAYER_CARD));

        frame.add(cards,BorderLayout.CENTER);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);  //Once the application is opened maximize the window
        frame.setVisible(true); // makes the window visible to the user
    }

    /**
     * Call this after a successful login/signup to route users to their dashboard.
     */
    public void showDashboardForRole(Role role) {
        if (role == null) {
            return;
        }
        switch (role) {
            case FAN:
                showFanDashboard();
                break;
            case COACH:
                cardLayout.show(cards, COACH_CARD);
                break;
            case PLAYER:
                cardLayout.show(cards, PLAYER_CARD);
                break;
            default:
                cardLayout.show(cards, HOME_CARD);
        }
    }

    public void showFanDashboard() {
        cardLayout.show(cards, FAN_DASHBOARD_CARD);
    }

    public FanDashboardPanel getFanDashboardPanel() {
        return fanDashboardPanel;
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> new SportsMetrics().CreateGUI());
    }
}
