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
        cards.add(new PlayerPanel(cardLayout,cards),"Player Page");


        frame.add(cards,BorderLayout.CENTER);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);  //Once the application is opened maximize the window
        frame.setVisible(true); // makes the window visible to the user

    }


    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> new SportsMetrics().CreateGUI());
    }
}



