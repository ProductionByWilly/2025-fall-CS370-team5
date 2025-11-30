import java.awt.*;
import javax.swing.*;

public class PollsPanel extends BasePanel {
    public PollsPanel(CardLayout cardLayout, JPanel cards) {
        super("Polls Page", cardLayout, cards);
        setLayout(new BorderLayout());

        add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel title = new JLabel("Fan Polls");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(Color.WHITE);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createQuestionContainer(200,450,850,150));
        add(createQuestionContainer(200,620,850,200));

        add(Box.createVerticalGlue());
    }

    private JPanel CreateSquare(int x,int y,int w,int h){
        JPanel pollPanel = new JPanel();
        pollPanel.setBackground(AppColors.Blue);
        pollPanel.setBounds(x,y,w,h);

        JLabel smallDescritionLabel = new JLabel("Cast your vote");
        smallDescritionLabel.setBounds(120,120,200,20);
        smallDescritionLabel.setForeground(Color.white);
        smallDescritionLabel.setFont(new Font("Arial",Font.BOLD,15));
        add(smallDescritionLabel);

        JLabel pollsLabel = new JLabel("Poll & Predictions");
        pollsLabel.setFont(new Font("Arial",Font.BOLD,25));
        pollsLabel.setForeground(Color.white);
        pollsLabel.setBounds(100,80,300,50);
        add(pollsLabel);

        add(pollPanel);
        return pollPanel;
    }

    private JPanel createQuestionContainer(int x,int y,int w,int h)
    {
        JPanel QuestionContainer = new JPanel();
        setLayout(null);
        QuestionContainer.setBackground(AppColors.Blue);
        QuestionContainer.setAlignmentX(LEFT_ALIGNMENT);
        QuestionContainer.setBounds(x,y,w,h);

        JLabel PollLabel = new JLabel("Current Polls");
        PollLabel.setForeground(Color.white);
        PollLabel.setFont(new Font("Arial",Font.BOLD,25));
        PollLabel.setBounds(100,380,250,100);
        add(PollLabel);

        JLabel questionInfo = new JLabel("Will CSU San Marcos win the next game?");
        questionInfo.setForeground(Color.white);
        questionInfo.setBounds(0,20,200,30);
        questionInfo.setFont(new Font("Arial",Font.PLAIN,20));
        QuestionContainer.add(questionInfo);

        JPanel topbar = new JPanel();
        topbar.setBackground(AppColors.Blue);
        topbar.setBounds(0,0, 1600, 75);

        JButton BackToDashboard = new JButton("Back to Fan Dashboard");
        BackToDashboard.setBounds(200,80,100,50);
        topbar.add(BackToDashboard);

        JButton LogOut = new JButton("Log Out");
        LogOut.setBounds(300, 10, 50, 20);
        topbar.add(LogOut);
        add(topbar);

        add(CreateSquare(200,150,320,200));
        add(CreateSquare(550,150,320,200));
        add(CreateSquare(900,150,320,200));

        add(voteButton("Yes",220,500,800,30));
        add(voteButton("No", 220, 550, 800, 30));
        add(createRightSquareColumn());
  
        return QuestionContainer;
    }

    private JPanel createRightSquareColumn()
    {
        JPanel rightSquare = new JPanel();
        rightSquare.setBackground(AppColors.Blue);
        rightSquare.setBounds(1150,450,350,300);
        add(rightSquare);

        return rightSquare;
    }

    private JButton voteButton(String buttonName,int x,int y,int w,int h)
    {
        JButton votingButton = new JButton(buttonName);
        votingButton.setForeground(Color.white);
        votingButton.setBackground(AppColors.darkTeal);
        votingButton.setBounds(x,y,w,h);
        add(votingButton);

        return votingButton;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Polls Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            
            CardLayout cardLayout = new CardLayout();
            JPanel cards = new JPanel(cardLayout);
            
            PollsPanel panel = new PollsPanel(cardLayout, cards);
            frame.add(panel);
            
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

    }
}
