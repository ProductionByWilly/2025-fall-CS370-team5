import java.awt.*;
import javax.swing.*;
import java.net.URL;

public class PollsPanel extends BasePanel {
    public PollsPanel(CardLayout cardLayout, JPanel cards) {
        super("Polls Page", cardLayout, cards);

        // Page layout setup
        setLayout(new BorderLayout());
        setBackground(AppColors.darkTeal);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(16, 40, 10, 40));

        // Title + subtitle
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JLabel title = new JLabel("Polls & Predictions", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Cast your vote on upcoming games", SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 14f));
        subtitle.setForeground(new Color(200, 210, 230));

        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 4)));
        header.add(subtitle);

        topBar.add(header, BorderLayout.CENTER);

        // Back button navigation
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        navButtons.setOpaque(false);

        JButton backToFanButton = new JButton("Back to Fan Dashboard");
        backToFanButton.addActionListener(e -> cardLayout.show(cards, "Fan Page"));

        navButtons.add(backToFanButton);
        topBar.add(navButtons, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Main vertical content container with padding
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));


        // Top row: 3 summary/result cards
        JPanel resultsRow = new JPanel(new GridLayout(1, 3, 20, 0));
        resultsRow.setOpaque(false);

                resultsRow.add(createResultCard(
                "Game Outcome",
                "--",
                "Yes/No results for next-game winner"
        ));
        resultsRow.add(createResultCard(
                "Total Points",
                "--",
                "Over/Under vote on combined score"
        ));
        resultsRow.add(createResultCard(
                "Player of the Game",
                "--",
                "Fan pick for standout Cougar"
        ));

        content.add(resultsRow);
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        // Label for poll section
        JLabel pollsHeader = new JLabel("Current Polls");
        pollsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        pollsHeader.setFont(pollsHeader.getFont().deriveFont(Font.BOLD, 18f));
        pollsHeader.setForeground(Color.WHITE);
        content.add(pollsHeader);
        content.add(Box.createRigidArea(new Dimension(0, 12)));

        // Poll cards stacked vertically
        JPanel pollsColumn = new JPanel();
        pollsColumn.setLayout(new BoxLayout(pollsColumn, BoxLayout.Y_AXIS));
        pollsColumn.setOpaque(false);

        pollsColumn.add(createPollCard("Will CSU San Marcos win the next game?"));
        pollsColumn.add(Box.createRigidArea(new Dimension(0, 12)));
        pollsColumn.add(createPollCard("Will the Cougars score 70+ points in the next game?"));
        pollsColumn.add(Box.createRigidArea(new Dimension(0, 12)));
                pollsColumn.add(createPlayerPollCard(
                "Who will be Player of the Game?",
                new String[]{
                        "Dexter Stratton",
                        "Micah Boone",
                        "Keavie Love",
                        "Kyelin King",
                        "Donovan Watkins",
                        "Will Harris",
                        "Tracy Bryden"
                }
        ));

        // Left-side placeholder image for live poll stats
        JPanel graphPanel = new JPanel();
        graphPanel.setOpaque(false);
        graphPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24)); // space between image and polls

        JLabel graphLabel = new JLabel();
        graphLabel.setPreferredSize(new Dimension(320, 220));

        // Load image from resources
        URL graphUrl = getClass().getResource("/Resources/images/barPlaceholder.png"); 

        if (graphUrl != null) {
            ImageIcon icon = new ImageIcon(graphUrl);
            // Scale to fit the reserved area
            Image scaled = icon.getImage().getScaledInstance(320, 220, Image.SCALE_SMOOTH);
            graphLabel.setIcon(new ImageIcon(scaled));
        } else {
            // Fallback text if image not found
            graphLabel.setText("Poll stats preview");
            graphLabel.setForeground(Color.WHITE);
        }

        graphPanel.add(graphLabel);

        // Wrap image + polls into a horizontal row
        JPanel pollsRow = new JPanel();
        pollsRow.setLayout(new BoxLayout(pollsRow, BoxLayout.X_AXIS));
        pollsRow.setOpaque(false);

        pollsRow.add(graphPanel);
        pollsRow.add(pollsColumn);

        content.add(pollsRow);
        content.add(Box.createVerticalGlue());

        add(content, BorderLayout.CENTER);
    }

    private JPanel createResultCard(String title, String value, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppColors.darkBlue);
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 18, 18));
        card.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 22f));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(descLabel.getFont().deriveFont(Font.PLAIN, 12f));
        descLabel.setForeground(new Color(200, 210, 230));

        card.add(titleLabel);
        card.add(valueLabel);
        card.add(descLabel);

        return card;

    }

    private JPanel createPollCard(String questionText) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppColors.darkBlue);
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 18, 18));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel questionLabel = new JLabel("<html>" + questionText + "</html>");
        questionLabel.setFont(questionLabel.getFont().deriveFont(Font.PLAIN, 14f));
        questionLabel.setForeground(Color.WHITE);

        card.add(questionLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonsRow.setOpaque(false);

        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        // Visual-only for now; logic will be added later
        buttonsRow.add(yesButton);
        buttonsRow.add(noButton);
        card.add(buttonsRow);
        return card;

    }

        private JPanel createPlayerPollCard(String questionText, String[] playerNames) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppColors.darkBlue);
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 18, 18));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel questionLabel = new JLabel("<html>" + questionText + "</html>");
        questionLabel.setFont(questionLabel.getFont().deriveFont(Font.PLAIN, 14f));
        questionLabel.setForeground(Color.WHITE);

        card.add(questionLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Use a vertical list of radio buttons
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        playersPanel.setOpaque(false);

        ButtonGroup group = new ButtonGroup();

        for (String name : playerNames) {
            JRadioButton playerOption = new JRadioButton(name);
            playerOption.setOpaque(false);
            playerOption.setForeground(Color.WHITE);
            playerOption.setFont(playerOption.getFont().deriveFont(Font.PLAIN, 13f));

            group.add(playerOption);
            playersPanel.add(playerOption);
        }

        card.add(playersPanel);

        return card;
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
