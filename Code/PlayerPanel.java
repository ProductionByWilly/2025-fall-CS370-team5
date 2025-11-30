import Database.AuthService;
import Database.Role;
import Database.User;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/*
 * Player panel has two states:
 *   1) Login screen
 *   2) Player dashboard after successful login
 */
public class PlayerPanel extends BasePanel {

    // ---- Login state ----
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    private JPanel loginBox;
    private JPanel dashboardBox;
    private JLabel dashWelcomeLabel;

    // (reuse your existing AuthService)
    private final AuthService auth = new AuthService();

    // ---- Dashboard widgets ----
    private static final Color DASH_BACKGROUND = new Color(15, 23, 42);      // dark navy
    private static final Color DASH_MUTED_TEXT = new Color(148, 163, 184);   // slate 400
    private static final Color DASH_PURPLE     = new Color(124, 58, 237);    // header gradient-ish

    // quick stat labels
    private JLabel ppgValueLabel;
    private JLabel rpgValueLabel;
    private JLabel apgValueLabel;
    private JLabel ratingValueLabel;

    // performance table
    private JTable performanceTable;
    private DefaultTableModel performanceTableModel;

    public PlayerPanel(CardLayout cardLayout, JPanel cards) {
        super("Player Page", cardLayout, cards);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppColors.darkTeal); // same outer bg as FanPanel

        add(Box.createRigidArea(new Dimension(0, 10)));

        createLoginBox();
        add(loginBox);
        add(Box.createRigidArea(new Dimension(0, 20)));

        createDashboardBox();
        add(dashboardBox);

        dashboardBox.setVisible(false); // start on login
    }

    // ---------------- LOGIN UI ----------------

    private void createLoginBox() {
        loginBox = new JPanel(new GridBagLayout());
        loginBox.setBackground(AppColors.darkTeal);
        loginBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel card = new JPanel(new BorderLayout(24, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        card.setPreferredSize(new Dimension(900, 340));

        // Left: small info section (could be an image later)
        JPanel leftPanel = new JPanel(new BorderLayout(0, 12));
        leftPanel.setBackground(Color.WHITE);

        JLabel infoTitle = new JLabel("Player Portal");
        infoTitle.setFont(infoTitle.getFont().deriveFont(Font.BOLD, 18f));

        JLabel infoText = new JLabel(
                "<html>Track your performance, skills, goals, and achievements " +
                        "for the CSU Cougars season.</html>"
        );
        infoText.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel infoBox = new JPanel();
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setBackground(Color.WHITE);
        infoBox.add(infoTitle);
        infoBox.add(Box.createRigidArea(new Dimension(0, 8)));
        infoBox.add(infoText);

        leftPanel.add(infoBox, BorderLayout.NORTH);

        // Right: login form
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JLabel loginTitle = new JLabel("Player Login");
        loginTitle.setFont(loginTitle.getFont().deriveFont(Font.BOLD, 22f));
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(loginTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Email
        JPanel emailRow = new JPanel();
        emailRow.setBackground(Color.WHITE);
        emailRow.setLayout(new BoxLayout(emailRow, BoxLayout.X_AXIS));

        JLabel emailLabel = new JLabel("Email: ");
        emailField = new JTextField(30);
        emailField.setMaximumSize(new Dimension(400, 25));

        emailRow.add(emailLabel);
        emailRow.add(Box.createRigidArea(new Dimension(10, 0)));
        emailRow.add(emailField);
        emailRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(emailRow);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password
        JPanel passRow = new JPanel();
        passRow.setBackground(Color.WHITE);
        passRow.setLayout(new BoxLayout(passRow, BoxLayout.X_AXIS));

        JLabel passLabel = new JLabel("Password: ");
        passwordField = new JPasswordField(30);
        passwordField.setMaximumSize(new Dimension(400, 25));

        passRow.add(passLabel);
        passRow.add(Box.createRigidArea(new Dimension(10, 0)));
        passRow.add(passwordField);
        passRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(passRow);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(messageLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Buttons row
        JPanel buttonRow = new JPanel();
        buttonRow.setBackground(Color.WHITE);
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));

        JButton loginButton = new JButton("Login");
        JButton backButton  = new JButton("Back to Main");

        loginButton.addActionListener(e -> handleLogin());
        backButton.addActionListener(e -> cardLayout.show(cards, "Home Page"));

        buttonRow.add(loginButton);
        buttonRow.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonRow.add(backButton);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(buttonRow);
        rightPanel.add(Box.createVerticalGlue());

        card.add(leftPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginBox.add(card, gbc);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String pwd   = new String(passwordField.getPassword());

        try {
            User u = auth.signIn(email, pwd);

            if (u.role != Role.PLAYER) {
                showError("This account is not a Player account.");
                return;
            }

            dashWelcomeLabel.setText("Welcome, " + u.email + " (Player)");

            // In the future: load player stats from DB here
            loadDashboardSampleData();

            loginBox.setVisible(false);
            dashboardBox.setVisible(true);
            revalidate();
            repaint();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String msg) {
        messageLabel.setForeground(Color.RED);
        messageLabel.setText(msg);
    }

    private void handleLogout() {
        if (emailField != null) emailField.setText("");
        if (passwordField != null) passwordField.setText("");
        if (messageLabel != null) messageLabel.setText(" ");

        loginBox.setVisible(true);
        dashboardBox.setVisible(false);

        cardLayout.show(cards, "Home Page");
    }

    // --------------- DASHBOARD UI ----------------

    private void createDashboardBox() {
        dashboardBox = new JPanel();
        dashboardBox.setLayout(new BoxLayout(dashboardBox, BoxLayout.Y_AXIS));
        dashboardBox.setBackground(AppColors.darkTeal);
        dashboardBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        dashboardBox.add(Box.createRigidArea(new Dimension(0, 10)));

        dashWelcomeLabel = new JLabel("Welcome, Player!");
        dashWelcomeLabel.setForeground(Color.WHITE);
        dashWelcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashWelcomeLabel.setFont(dashWelcomeLabel.getFont().deriveFont(Font.BOLD, 20f));

        JLabel subtitle = new JLabel("View performance trends, skills, goals, and achievements");
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        dashboardBox.add(dashWelcomeLabel);
        dashboardBox.add(Box.createRigidArea(new Dimension(0, 5)));
        dashboardBox.add(subtitle);
        dashboardBox.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel inner = createDashboardPanel();
        inner.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.setPreferredSize(new Dimension(1100, 650));

        dashboardBox.add(inner);
        dashboardBox.add(Box.createVerticalGlue());
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DASH_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        panel.add(createHeaderCard(), BorderLayout.NORTH);
        panel.add(createMainContent(), BorderLayout.CENTER);

        return panel;
    }

    // Top big player card
    private JPanel createHeaderCard() {
        JPanel card = new JPanel(new BorderLayout(24, 0));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        card.setBackground(DASH_PURPLE);

        // Left: avatar circle
        JPanel avatarWrapper = new JPanel(new BorderLayout());
        avatarWrapper.setOpaque(false);

        // simple placeholder image (could swap for real player headshot)
        JLabel avatar = new JLabel();
        avatar.setPreferredSize(new Dimension(120, 120));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(30, 64, 175)); // purple-ish blue
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setText("KD");
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 32));

        avatarWrapper.add(avatar, BorderLayout.CENTER);

        // Center: name + position
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("Kevin Durant");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        nameLabel.setForeground(Color.WHITE);

        JLabel posLabel = new JLabel("Small Forward  •  #35");
        posLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        posLabel.setForeground(new Color(224, 231, 255));

        center.add(nameLabel);
        center.add(Box.createRigidArea(new Dimension(0, 8)));
        center.add(posLabel);

        // Right: stats (PPG, RPG, APG, Rating)
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);

        ppgValueLabel    = new JLabel("28.5", SwingConstants.CENTER);
        rpgValueLabel    = new JLabel("7.2",  SwingConstants.CENTER);
        apgValueLabel    = new JLabel("5.8",  SwingConstants.CENTER);
        ratingValueLabel = new JLabel("95",   SwingConstants.CENTER);

        statsRow.add(createHeaderStatCard("PPG", ppgValueLabel));
        statsRow.add(createHeaderStatCard("RPG", rpgValueLabel));
        statsRow.add(createHeaderStatCard("APG", apgValueLabel));
        statsRow.add(createHeaderStatCard("Rating", ratingValueLabel));

        card.add(avatarWrapper, BorderLayout.WEST);
        card.add(center, BorderLayout.CENTER);
        card.add(statsRow, BorderLayout.EAST);

        return card;
    }

    private JPanel createHeaderStatCard(String label, JLabel value) {
        JPanel card = new JPanel();
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setForeground(new Color(224, 231, 255));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        value.setForeground(Color.WHITE);
        value.setFont(new Font("SansSerif", Font.BOLD, 20));
        value.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lbl);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(value);

        return card;
    }

    // Main content: tabs (performance, skills, training, achievements)
    private JComponent createMainContent() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabs.setBackground(DASH_BACKGROUND);
        tabs.setForeground(Color.WHITE);
        tabs.setOpaque(true);
        tabs.setBorder(BorderFactory.createEmptyBorder());

        tabs.addTab("Performance", createPerformanceTab());
        tabs.addTab("Skills", createSkillsTab());
        tabs.addTab("Training Goals", createTrainingGoalsTab());
        tabs.addTab("Achievements", createAchievementsTab());

        return tabs;
    }

    // ---------- Performance Tab ----------
    private JPanel createPerformanceTab() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        // Title
        JLabel title = new JLabel("Recent Performance");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        panel.add(title, BorderLayout.NORTH);

        // Table of last games (simpler than fancy charts)
        String[] columns = {"Game", "Opponent", "Result", "PTS", "REB", "AST"};
        performanceTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        performanceTable = new JTable(performanceTableModel);
        performanceTable.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(performanceTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(DASH_BACKGROUND);
        scroll.setOpaque(false);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ---------- Skills Tab ----------
    private JPanel createSkillsTab() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 16));

        JLabel title = new JLabel("Skills Assessment");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Current skill ratings across key areas");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(DASH_MUTED_TEXT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(subtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));

        // skills with progress bars (0–100)
        panel.add(createSkillRow("Shooting",     95));
        panel.add(createSkillRow("Defense",      88));
        panel.add(createSkillRow("Ball Handling",90));
        panel.add(createSkillRow("Passing",      85));
        panel.add(createSkillRow("Rebounding",   82));
        panel.add(createSkillRow("Speed",        87));

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createSkillRow(String label, int value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(value);
        bar.setStringPainted(true);
        bar.setString(value + "/100");

        row.add(lbl, BorderLayout.WEST);
        row.add(bar, BorderLayout.CENTER);

        return row;
    }

    // ---------- Training Goals Tab ----------
    private JPanel createTrainingGoalsTab() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 16));

        JLabel title = new JLabel("Training Goals");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Season targets and progress tracking");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(DASH_MUTED_TEXT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(subtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));

        panel.add(createGoalCard("Free Throw Accuracy", "Current: 88% • Target: 90%", 97));
        panel.add(createGoalCard("3-Point Percentage", "Current: 41% • Target: 43%", 95));
        panel.add(createGoalCard("Vertical Jump", "Current: 34\" • Target: 36\"", 94));
        panel.add(createGoalCard("Sprint Speed", "Current: 92% • Target: 95%", 96));

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createGoalCard(String title, String subtitle, int progressPercent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setBackground(new Color(15, 23, 42));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setForeground(DASH_MUTED_TEXT);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(progressPercent);
        bar.setStringPainted(true);
        bar.setString(progressPercent + "%");

        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(subLbl);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(bar);

        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        return card;
    }

    // ---------- Achievements Tab ----------
    private JPanel createAchievementsTab() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 16));

        JLabel title = new JLabel("Recent Achievements");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Awards and milestones");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(DASH_MUTED_TEXT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(subtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));

        panel.add(createAchievementCard("Player of the Week", "Nov 1, 2024", true));
        panel.add(createAchievementCard("30+ Point Streak", "Oct 28, 2024", true));
        panel.add(createAchievementCard("Team MVP", "Oct 15, 2024", true));

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createAchievementCard(String title, String date, boolean isNew) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setBackground(new Color(15, 23, 42));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel dateLbl = new JLabel(date);
        dateLbl.setForeground(DASH_MUTED_TEXT);
        dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(titleLbl);
        left.add(Box.createRigidArea(new Dimension(0, 4)));
        left.add(dateLbl);

        card.add(left, BorderLayout.CENTER);

        if (isNew) {
            JLabel badge = new JLabel("New");
            badge.setOpaque(true);
            badge.setBackground(new Color(147, 51, 234));
            badge.setForeground(Color.WHITE);
            badge.setFont(new Font("SansSerif", Font.BOLD, 11));
            badge.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
            card.add(badge, BorderLayout.EAST);
        }

        return card;
    }

    // For now this just fills in demo data.
    // Later you can replace with DB calls.
    private void loadDashboardSampleData() {
        // Performance games
        Object[][] rows = {
                {"Game 1", "vs Lakers",   "W", 32, 8, 7},
                {"Game 2", "vs Warriors", "W", 28, 7, 5},
                {"Game 3", "vs Clippers", "L", 35, 9, 6},
                {"Game 4", "vs Suns",     "W", 27, 6, 4},
                {"Game 5", "vs Kings",    "W", 30, 9, 8},
        };

        if (performanceTableModel != null) {
            performanceTableModel.setRowCount(0);
            for (Object[] r : rows) {
                performanceTableModel.addRow(r);
            }
        }

        // quick header stats – could be computed from DB later
        ppgValueLabel.setText("28.5");
        rpgValueLabel.setText("7.2");
        apgValueLabel.setText("5.8");
        ratingValueLabel.setText("95");
    }
}
