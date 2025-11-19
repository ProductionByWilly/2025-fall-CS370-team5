import java.awt.*;
import javax.swing.*;

public class MainPanel extends BasePanel {

    public MainPanel(CardLayout cardLayout,JPanel cards)
    {
        super("Main Page",cardLayout,cards);
        //Create a section for the banner on the top
        JPanel TopBanner = new JPanel();
        TopBanner.setLayout(null); // This allows us to chose the percise location for each item
        TopBanner.setBounds(0,0,1535 ,200); // setBounds is needed for each item or they will not display on the page
        
        //Adds the title to the page
        JLabel Title = new JLabel("CSU Sports Metrics");
        Title.setForeground(Color.white);
        Title.setFont(new Font("Arial",Font.BOLD,50));
        Title.setBounds(550,70,600,50);
        TopBanner.add(Title);
        add(TopBanner);

        //Adds text below the banner
        JLabel DescriptionChoice = new JLabel("Choose Your Dashboard", SwingConstants.CENTER);
        DescriptionChoice.setForeground(Color.white);
        DescriptionChoice.setFont(new Font("Arial", Font.BOLD, 25));
        DescriptionChoice.setBounds(150, 200, 1200, 50);
        add(DescriptionChoice);


        //Get the location for the image
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Resources/images/banner.png"));
        //Scale the image to the size we need to cover the banner
        Image ScaledIcon = imageIcon.getImage().getScaledInstance(1535, 200,Image.SCALE_SMOOTH);
        ImageIcon ScaledImage = new ImageIcon(ScaledIcon);
        //Add an image to the top banner
        JLabel image = new JLabel(ScaledImage);
        image.setBounds(0,0,1535 ,210);
        TopBanner.add(image);

        String FanDashboardLabel = "Fan Dashboard";
        String FanDescription = "<html>Track favorite teams, participate in polls and view the leaderboard</html>";
        String FanButtonLabel = "Continue as Fan";

        String CoachDashboardLabel = "Coach Dashboard";
        String CoachDescription = "<html>Manage team, view analytics, and manage stats</html>";
        String CoachButtonLabel = "Continue as Coach";

        String PlayerDashboardLabel = "Player Dashboard";
        String PlayerDescription = "<html>View and filter personal stats,view performance metrics and efficiency ratings</html>";
        String PlayerButtonLabel = "Continue as Player";

        //Create each section that is needed
        CreateSection(FanDashboardLabel,FanDescription,FanButtonLabel,50,300,350,200,AppColors.softBlue,"Fan Page");
        CreateSection(CoachDashboardLabel,CoachDescription,CoachButtonLabel,600,300,350,200,AppColors.green,"Coach Page");
        CreateSection(PlayerDashboardLabel,PlayerDescription,PlayerButtonLabel,1150,300,350,200,AppColors.magenta,"Player Page");

        SignUpSection();
    }

    //Helper function that is used to create each section
    public void CreateSection(String DashboardLabel,String DescriptionLabel,String ButtonLabel,int x,int y,int w,int h,Color BorderAndButtonColor,String CardName)
    {
        //Create a section for the fan section or a container with some information
        JPanel Section = new JPanel();
        Section.setLayout(null); // used for absolute positioning
        Section.setBounds(x,y,w,h); //X - positon, Y - height, width - width of the section, height - height of the section
        Section.setBackground(AppColors.darkTeal);
        Section.setBorder(BorderFactory.createLineBorder(BorderAndButtonColor,4,true));

        //The text to be added into the dashboard 
        JLabel Dashboard = new JLabel(DashboardLabel);
        Dashboard.setForeground(Color.white);
        Dashboard.setFont(new Font("Arial",Font.BOLD,18));
        Dashboard.setBounds(10,60,200,25);
        Section.add(Dashboard);

        //Html tag allow for the wrap around of text
        JLabel Description = new JLabel(DescriptionLabel);
        Description.setForeground(AppColors.lightBlue);
        Description.setBounds(10,85,250,50); //The width needs enough space to incorporate the entire text
        Section.add(Description);

        // Creating the fan button for the main panel
        JButton Button = new JButton(ButtonLabel);
        //Adds a border to the button
        Button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        //Change the color of the button word
        Button.setForeground(Color.white);
        //Change the Color of the button
        Button.setBackground(BorderAndButtonColor);
        //Add the fan button to the container storing the buttons horizontally in the page
        Button.setBounds(40, 150, 250, 35);
        Button.setAlignmentX(CENTER_ALIGNMENT);
        Button.addActionListener(e -> cardLayout.show(cards,CardName));
        Section.add(Button);

        //Add the text into the fan section/container
        add(Section);
    }

    public void SignUpSection()
    {
    //Create a sign up section to contain all information
        JPanel SignUpSection = new JPanel();
        SignUpSection.setLayout(null); // used for absolute positioning
        SignUpSection.setBounds(550, 550, 450, 240);
        SignUpSection.setBackground(AppColors.darkTeal);
        SignUpSection.setBorder(BorderFactory.createLineBorder(AppColors.lightBlue,4,true));

        JLabel SignUpLabel = new JLabel("Or Sign In");
        SignUpLabel.setForeground(Color.white);
        SignUpLabel.setBounds(12,20,90,20);
        SignUpLabel.setFont(new Font("Arial",Font.BOLD,18));
        SignUpSection.add(SignUpLabel);

        JLabel SignInDescription = new JLabel("Enter your credentials to sign up");
        SignInDescription.setForeground(AppColors.lightBlue);
        SignInDescription.setBounds(10, 40, 200, 20);
        SignUpSection.add(SignInDescription);

        // -- Subpanel for username label and text entry fields --
        //Email Label
        JLabel EmailLabel = new JLabel("Email");
        EmailLabel.setForeground(Color.WHITE);
        EmailLabel.setBounds(10,70,50,20);
        SignUpSection.add(EmailLabel);

        //Label for the password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(10,130,80,20);
        
        SignUpSection.add(passwordLabel);

        JButton SignUpButton = new JButton("Sign in");
        SignUpButton.setBounds(40,185,350,30);
        SignUpSection.add(SignUpButton);

        add(SignUpSection);
    }
}

