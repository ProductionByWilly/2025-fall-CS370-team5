import java.awt.*;
import javax.swing.*;

public class FanPanel extends BasePanel {

    public FanPanel(CardLayout cardLayout,JPanel cards)
    {
    // Super calls the constructor from the parent
    //In this case it will call the constructore for the base panel and use it 
    super("Fan Page",cardLayout,cards);

    //Back Button to go back to the Main Dashboard
        JButton backButton = new JButton("Previous Page");//Create a back button
        //Action listener to go back once the back button is pressed
        backButton.addActionListener(e -> cardLayout.show(cards,"Home Page"));
        add(backButton); //add the button
    }
}
