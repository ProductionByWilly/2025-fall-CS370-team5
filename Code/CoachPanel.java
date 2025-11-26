import java.awt.*;
import javax.swing.*;

public class CoachPanel extends BasePanel {

    public CoachPanel(CardLayout cardLayout,JPanel cards)
    {
    // Super calls the constructor from the parent
    //In this case it will call the constructor for the base panel and use it
    super("Coach Dashboard",cardLayout,cards);


    LeaderboardPanel leaderboard = new LeaderboardPanel();
    leaderboard.setBounds(100, 150, 600, 400);
    add(leaderboard);
    //Back Button to go back to the Main Dashboard
        JButton backButton = new JButton("Previous Page");//Create a back button
        //Action listener to go back once the back button is pressed
        backButton.addActionListener(e -> cardLayout.show(cards,"Home Page"));
        add(backButton); //add the button
    }
}

