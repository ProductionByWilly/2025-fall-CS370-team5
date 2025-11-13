import java.awt.*;
import javax.swing.*;

public class MainPanel extends BasePanel {

    public MainPanel(CardLayout cardLayout,JPanel cards)
    {
        // Super calls the constructor from the parent
        //In this case it will call the constructore for the base panel and use it 
        super("Main Page",cardLayout,cards);
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
    }
}