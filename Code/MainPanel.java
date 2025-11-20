import java.awt.*;
import javax.swing.*;

public class MainPanel extends BasePanel {
    public MainPanel(CardLayout cardLayout, JPanel cards) {
        super("Main Page", cardLayout, cards);
        setLayout(new BorderLayout());
        
        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(AppColors.darkTeal);
        
        // Add top banner
        contentPanel.add(topBanner());
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Add description
        contentPanel.add(createDescription());
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Add dashboard sections
        JPanel sectionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        sectionsPanel.setBackground(AppColors.darkTeal);
        sectionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        
        String FanDashboardLabel = "Fan Dashboard";
        String FanDescription = "<html>Track favorite teams, participate in polls and view the leaderboard</html>";
        String FanButtonLabel = "Continue as Fan";
        String FanSymbolPath = "/Resources/images/FanIcon.jpg";

        String CoachDashboardLabel = "Coach Dashboard";
        String CoachDescription = "<html>Manage team, view analytics, and manage stats</html>";
        String CoachButtonLabel = "Continue as Coach";
        String CoachSymbolPath = "/Resources/images/TrophyIcon.png";

        String PlayerDashboardLabel = "Player Dashboard";
        String PlayerDescription = "<html>View and filter personal stats,view performance metrics and efficiency ratings</html>";
        String PlayerButtonLabel = "Continue as Player";
        
        sectionsPanel.add(CreateSection(FanDashboardLabel, FanDescription, FanButtonLabel, 
                                       AppColors.softBlue, "Fan Page", FanSymbolPath));
        sectionsPanel.add(CreateSection(CoachDashboardLabel, CoachDescription, CoachButtonLabel, 
                                       AppColors.green, "Coach Page", CoachSymbolPath));
        sectionsPanel.add(CreateSection(PlayerDashboardLabel, PlayerDescription, PlayerButtonLabel, 
                                       AppColors.magenta, "Player Page", CoachSymbolPath));
        
        contentPanel.add(sectionsPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Add sign-up section
        contentPanel.add(SignUpSection());
        contentPanel.add(Box.createVerticalGlue());
        
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel topBanner(){
        //Create a section for the banner on the top
        JPanel TopBanner = new JPanel(new BorderLayout());
        TopBanner.setPreferredSize(new Dimension(0,200));
        TopBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        //Adds the title to the page
        JLabel Title = new JLabel("CSU Sports Metrics",SwingConstants.CENTER);
        Title.setForeground(Color.white);
        Title.setFont(new Font("Arial",Font.BOLD,50));
        TopBanner.add(Title);
        
        //Get the location for the image
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Resources/images/banner.png"));
        //Scale the image to the size we need to cover the banner
        Image ScaledIcon = imageIcon.getImage().getScaledInstance(1535, 200,Image.SCALE_SMOOTH);
        ImageIcon ScaledImage = new ImageIcon(ScaledIcon);
        //Add an image to the top banner
        JLabel image = new JLabel(ScaledImage);
        TopBanner.add(image);

        return TopBanner;
    }

    private JLabel createDescription()
    {
        //Adds text below the banner
        JLabel DescriptionChoice = new JLabel("Choose Your Dashboard", SwingConstants.CENTER);
        DescriptionChoice.setForeground(Color.white);
        DescriptionChoice.setFont(new Font("Arial", Font.BOLD, 25));
        DescriptionChoice.setAlignmentX(Component.CENTER_ALIGNMENT);
        DescriptionChoice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return DescriptionChoice;
    }

    //Helper function that is used to create each section
    private JPanel CreateSection(String DashboardLabel,String DescriptionLabel,String ButtonLabel,Color BorderAndButtonColor,
        String CardName,String path)
    {
        //Create a section for the fan section or a container with some information
        JPanel Section = new JPanel();
        Section.setLayout(new BoxLayout(Section, BoxLayout.Y_AXIS)); // used for absolute positioning
        Section.setPreferredSize(new Dimension(350, 200)); //X - positon, Y - height, width - width of the section, height - height of the section
        Section.setBackground(AppColors.darkTeal);
        Section.setBorder(BorderFactory.createLineBorder(BorderAndButtonColor,4,true));

        //Create a space for the image to be placed
        JPanel TopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        TopPanel.setBackground(AppColors.darkTeal);

        //Get the location for the image by passing it from the main function
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
        //Scale the image
        Image ScaledIcon = imageIcon.getImage().getScaledInstance(50, 45,Image.SCALE_SMOOTH);
        ImageIcon ScaledImage = new ImageIcon(ScaledIcon); 
        JLabel image = new JLabel(ScaledImage);
        //Adding the image to the space we created for the image
        TopPanel.add(image);

        //Add the image into the section
        Section.add(TopPanel);

        //The text to be added into the dashboard 
        JLabel Dashboard = new JLabel(DashboardLabel);
        Dashboard.setForeground(Color.white);
        Dashboard.setFont(new Font("Arial",Font.BOLD,18));
        Dashboard.setAlignmentX(Component.LEFT_ALIGNMENT);
        Dashboard.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        Section.add(Dashboard);


        //Html tag allow for the wrap around of text
        JLabel Description = new JLabel(DescriptionLabel);
        Description.setForeground(AppColors.lightBlue);
        Description.setAlignmentX(Component.LEFT_ALIGNMENT);
        Description.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); //The width needs enough space to incorporate the entire text
        Section.add(Description);

        Section.add(Box.createVerticalGlue());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        buttonPanel.setBackground(AppColors.darkTeal);

        // Creating the fan button for the main panel
        JButton Button = new JButton(ButtonLabel);
        //Adds a border to the button
        Button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        //Change the color of the button word
        Button.setForeground(Color.white);
        //Change the Color of the button
        Button.setBackground(BorderAndButtonColor);
        //Add the fan button to the container storing the buttons horizontally in the page
        Button.setPreferredSize(new Dimension(250, 35));
        Button.addActionListener(e -> cardLayout.show(cards,CardName));
        Section.add(Button);
        //Add the text into the fan section/container
        return Section;
    }

    private JPanel SignUpSection()
    {
    //Create a sign up section to contain all information
        JPanel SignUpSection = new JPanel();
        SignUpSection.setLayout(new BoxLayout(SignUpSection, BoxLayout.Y_AXIS));
        SignUpSection.setPreferredSize(new Dimension(450, 240));
        SignUpSection.setMaximumSize(new Dimension(450, 240));
        SignUpSection.setBackground(AppColors.darkTeal);
        SignUpSection.setBorder(BorderFactory.createLineBorder(AppColors.lightBlue,4,true));
        SignUpSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel SignUpLabel = new JLabel("Or Sign In");
        SignUpLabel.setForeground(Color.white);
        SignUpLabel.setFont(new Font("Arial",Font.BOLD,18));
        SignUpLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        SignUpLabel.setBorder(BorderFactory.createEmptyBorder(20, 12, 5, 0));
        SignUpSection.add(SignUpLabel);

        JLabel SignInDescription = new JLabel("Enter your credentials to sign up");
        SignInDescription.setForeground(AppColors.lightBlue);
        SignInDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        SignInDescription.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        SignUpSection.add(SignInDescription);

        // -- Subpanel for username label and text entry fields --
        //Email Label
        JLabel EmailLabel = new JLabel("Email");
        EmailLabel.setForeground(Color.WHITE);
        EmailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        EmailLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 0));
        SignUpSection.add(EmailLabel);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setBackground(AppColors.lightGray);
        usernamePanel.setPreferredSize(new Dimension(400, 22));
        usernamePanel.setMaximumSize(new Dimension(400, 22));
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        SignUpSection.add(usernamePanel);

        SignUpSection.add(Box.createVerticalStrut(10));

        //Label for the password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 0));
        SignUpSection.add(passwordLabel);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setBackground(AppColors.lightGray);
        passwordPanel.setPreferredSize(new Dimension(400, 22));
        passwordPanel.setMaximumSize(new Dimension(400, 22));
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        SignUpSection.add(passwordPanel);

        JButton SignUpButton = new JButton("Sign in");
        SignUpButton.setBackground(AppColors.lightGray);
        SignUpButton.setForeground(Color.WHITE);
        SignUpButton.setPreferredSize(new Dimension(350, 30));
        SignUpButton.setMaximumSize(new Dimension(350, 30));
        SignUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        SignUpSection.add(SignUpButton);


        return SignUpSection;
    }

}

