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
/* 
    private JPanel loginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(AppColors.background);

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
        loginPanel.add(usernamePanel);

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
        loginPanel.add(passwordPanel);

        return loginPanel;
    }
*/