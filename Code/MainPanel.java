import Database.AuthService;
import Database.Role;
import Database.User;
import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class MainPanel extends BasePanel {

    // Sign up UI state 
    private JTextField signUpEmailField;
    private JPasswordField signUpPasswordField;
    private JComboBox<String> signUpRoleBox;
    private JLabel signUpMessageLabel;

    // Auth service (DB + BCrypt) 
    private final AuthService auth = new AuthService();

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
        JPanel sectionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        sectionsPanel.setBackground(AppColors.darkTeal);
        sectionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        
        String FanDashboardLabel = "Fan";
        String FanDescription = "<html>Track favorite teams, participate in polls and view the leaderboard</html>";
        String FanButtonLabel = "Continue as Fan";
        String FanSymbolPath = "/Resources/images/FanImage.png";

        String CoachDashboardLabel = "Coach";
        String CoachDescription = "<html>Manage team, view analytics, and manage stats</html>";
        String CoachButtonLabel = "Continue as Coach";
        String CoachSymbolPath = "/Resources/images/CoachImage.png";

        String PlayerDashboardLabel = "Player";
        String PlayerDescription = "<html>View and filter personal stats,view performance metrics and efficiency ratings</html>";
        String PlayerButtonLabel = "Continue as Player";
        String PlayerSymbolPath = "/Resources/images/TrophyIcon.png";
        
        sectionsPanel.add(CreateSection(FanDashboardLabel, FanDescription, FanButtonLabel, 
                                       AppColors.softBlue, "Fan Page", FanSymbolPath));
        sectionsPanel.add(CreateSection(CoachDashboardLabel, CoachDescription, CoachButtonLabel, 
                                       AppColors.green, "Coach Page", CoachSymbolPath));
        sectionsPanel.add(CreateSection(PlayerDashboardLabel, PlayerDescription, PlayerButtonLabel, 
                                       AppColors.magenta, "Player Page", PlayerSymbolPath));
        
        contentPanel.add(sectionsPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Add sign-up section
        contentPanel.add(SignUpSection());
        contentPanel.add(Box.createVerticalGlue());
        
        // Attach assembled UI to the frame
        add(contentPanel, BorderLayout.CENTER);
    }
       
    private JPanel topBanner(){
        //Create a section for the banner on the top
        JPanel TopBanner = new JPanel(new BorderLayout());
        TopBanner.setPreferredSize(new Dimension(0,200));
        TopBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        //Get the location for the image
        URL bannerUrl = getClass().getResource("/Resources/images/banner.png");
        JLabel image = new JLabel();
        if (bannerUrl != null) {
            ImageIcon imageIcon = new ImageIcon(bannerUrl);
            image.setIcon(imageIcon);

            // Set the text on the same label
            image.setText("CSU Sports Metrics");   
            image.setHorizontalAlignment(SwingConstants.CENTER);
            image.setVerticalAlignment(SwingConstants.CENTER);
            image.setForeground(Color.WHITE);
            image.setFont(new Font("Arial", Font.BOLD, 50));
            image.setHorizontalTextPosition(SwingConstants.CENTER);
            image.setVerticalTextPosition(SwingConstants.CENTER);
        } else {

            System.err.println("banner.png not found on classpath (/Resources/images/banner.png)");

            image.setText("CSU Sports Metrics");
            image.setHorizontalAlignment(SwingConstants.CENTER);
            image.setForeground(Color.WHITE);
            image.setFont(new Font("Arial", Font.BOLD, 50));
        }
        TopBanner.add(image, BorderLayout.CENTER);

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
        Section.setPreferredSize(new Dimension(340, 180)); //X - positon, Y - height, width - width of the section, height - height of the section
        Section.setBackground(AppColors.darkTeal);
        Section.setBorder(BorderFactory.createLineBorder(BorderAndButtonColor,4,true));

        //Create a space for the image to be placed
        JPanel TopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        TopPanel.setBackground(AppColors.darkTeal);

        //Get the location for the image by passing it from the main function
        URL iconUrl = getClass().getResource(path);
        JLabel image = new JLabel();
        if (iconUrl != null) {
            ImageIcon imageIcon = new ImageIcon(iconUrl);
            //Scale the image
            Image ScaledIcon = imageIcon.getImage().getScaledInstance(50, 45,Image.SCALE_SMOOTH);
            ImageIcon ScaledImage = new ImageIcon(ScaledIcon); 
            image.setIcon(ScaledImage);
        } else {
            System.err.println("⚠️  icon not found on classpath (" + path + ")");
            image.setText("icon");
            image.setForeground(Color.WHITE);
        }
        //Adding the image to the space we created for the image
        TopPanel.add(image);

        //Add the image into the section
        Section.add(TopPanel);

        //The text to be added into the dashboard 
        JLabel Dashboard = new JLabel(DashboardLabel);
        Dashboard.setForeground(Color.white);
        Dashboard.setFont(new Font("Arial",Font.BOLD,18));
        Dashboard.setAlignmentX(Component.LEFT_ALIGNMENT);
        Dashboard.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 180));
        Section.add(Dashboard);


        //Html tag allow for the wrap around of text
        JLabel Description = new JLabel(DescriptionLabel);
        Description.setForeground(AppColors.lightBlue);
        Description.setAlignmentX(Component.LEFT_ALIGNMENT);
        Description.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 60)); //The width needs enough space to incorporate the entire text
        Section.add(Description);

        Section.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        buttonPanel.setBackground(AppColors.darkTeal);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));


        // Creating the fan button for the main panel
        JButton Button = new JButton(ButtonLabel);
        //Adds a border to the button
        Button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        Button.setBorder(BorderFactory.createEmptyBorder(5,0,5,15));//internal padding
        //Change the color of the button word
        Button.setForeground(Color.white);
        //Change the Color of the button
        Button.setBackground(BorderAndButtonColor);
        //Add the fan button to the container storing the buttons horizontally in the page
        Button.setMaximumSize(new Dimension(300, 40));
        Button.addActionListener(e -> cardLayout.show(cards,CardName));
        Section.add(Button);

        Section.add(Box.createVerticalStrut(30));
        //Add the text into the fan section/container
        return Section;
    }


    // Create a section for the signup
    private JPanel SignUpSection()
    {
        // Create a sign up section to contain all information
        JPanel SignUpSection = new JPanel();
        SignUpSection.setLayout(new BoxLayout(SignUpSection, BoxLayout.Y_AXIS));
        SignUpSection.setPreferredSize(new Dimension(450, 300));
        SignUpSection.setMaximumSize(new Dimension(450, 300));
        SignUpSection.setBackground(AppColors.darkTeal);
        SignUpSection.setBorder(BorderFactory.createLineBorder(AppColors.lightBlue,4,true));
        SignUpSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel SignUpLabel = new JLabel("Or Sign Up");
        SignUpLabel.setForeground(Color.white);
        SignUpLabel.setFont(new Font("Arial",Font.BOLD,18));
        SignUpLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        SignUpLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        SignUpSection.add(SignUpLabel);

        JLabel SignInDescription = new JLabel("Enter your credentials to sign up");
        SignInDescription.setForeground(AppColors.lightBlue);
        SignInDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        SignInDescription.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 150));
        SignUpSection.add(SignInDescription);

        // ----- Email label + field -----
        JLabel EmailLabel = new JLabel("Email");
        EmailLabel.setForeground(Color.WHITE);
        EmailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        EmailLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 150));
        SignUpSection.add(EmailLabel);

        signUpEmailField = new JTextField();
        signUpEmailField.setPreferredSize(new Dimension(350, 24));
        signUpEmailField.setMaximumSize(new Dimension(350, 24));
        signUpEmailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        SignUpSection.add(signUpEmailField);

        SignUpSection.add(Box.createVerticalStrut(8));

        // ----- Password label + field -----
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 2, 0));
        SignUpSection.add(passwordLabel);

        signUpPasswordField = new JPasswordField();
        signUpPasswordField.setPreferredSize(new Dimension(350, 24));
        signUpPasswordField.setMaximumSize(new Dimension(350, 24));
        signUpPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        SignUpSection.add(signUpPasswordField);

        SignUpSection.add(Box.createVerticalStrut(8));

        // ----- Role label + dropdown -----
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 0));
        SignUpSection.add(roleLabel);

        signUpRoleBox = new JComboBox<>(new String[] { "Fan", "Player", "Coach" });
        signUpRoleBox.setPreferredSize(new Dimension(200, 24));
        signUpRoleBox.setMaximumSize(new Dimension(200, 24));
        signUpRoleBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        SignUpSection.add(signUpRoleBox);

        SignUpSection.add(Box.createVerticalStrut(8));

        // ----- Message label -----
        signUpMessageLabel = new JLabel(" ");
        signUpMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        SignUpSection.add(signUpMessageLabel);
        SignUpSection.add(Box.createVerticalStrut(5));

        SignUpSection.add(Box.createVerticalStrut(5)); // add spacing between the password panel and the button

        //horizontal panel to center the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(AppColors.darkTeal);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(300, 30));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(Box.createHorizontalStrut(5)); // Push things to the right


        // ----- Sign Up button -----
        JButton SignUpButton = new JButton("Sign Up");
        SignUpButton.setBackground(AppColors.lightGray);
        SignUpButton.setForeground(Color.BLACK);
        // Keep the button a normal control height so the section has room for the form fields
        SignUpButton.setPreferredSize(new Dimension(260, 30));
        SignUpButton.setMaximumSize(new Dimension(260, 30));
        SignUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(SignUpButton);
        buttonPanel.add(Box.createHorizontalGlue()); 
        SignUpSection.add(buttonPanel);


        // Click handler -> call AuthService.signUp(...)
        SignUpButton.addActionListener(e -> handleSignUp());

        return SignUpSection;
    }


     private void handleSignUp() {
        String email = signUpEmailField.getText().trim();
        String pwd   = new String(signUpPasswordField.getPassword());
        String roleText = (String) signUpRoleBox.getSelectedItem();

        // Map dropdown text -> Role enum
        Role role;
        if ("Fan".equalsIgnoreCase(roleText)) {
            role = Role.FAN;
        } else if ("Player".equalsIgnoreCase(roleText)) {
            role = Role.PLAYER;
        } else {
            role = Role.COACH;
        }

        try {
            User u = auth.signUp(email, pwd, role);

            signUpMessageLabel.setForeground(new Color(0, 200, 0));
            signUpMessageLabel.setText("Account created: " + u.email + " (" + u.role + ")");

            // OPTIONAL: immediately send them to the right panel
            switch (u.role) {
                case FAN    -> cardLayout.show(cards, "Fan Page");
                case PLAYER -> cardLayout.show(cards, "Player Page");
                case COACH  -> cardLayout.show(cards, "Coach Page");
            }

        } catch (IllegalArgumentException ex) {
            // validation error (bad email, weak password, already registered, etc.)
            signUpMessageLabel.setForeground(Color.RED);
            signUpMessageLabel.setText(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            signUpMessageLabel.setForeground(Color.RED);
            signUpMessageLabel.setText("Sign up failed. Please try again.");
        }
    }

}