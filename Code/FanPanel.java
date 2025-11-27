import Database.FanStatsDAO;
import Database.Role;
import Database.User;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/* Fan Panel page has two states: 
    1) Login screen
    2) Dashboard screen (after login)
*/
public class FanPanel extends BasePanel {

    // Login related fields
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    // Holds the two different states
    private JPanel loginBox;       // holds login UI
    private JPanel dashboardBox;   // holds dashboard UI
    private JLabel dashWelcomeLabel; // shown once user logs in

    // Uses auth backend
    private final AuthService auth = new AuthService();
    private final FanStatsDAO statsDAO = new FanStatsDAO();


    // ------------ Dashboard colors & table models ------------

    //  Darker background inside the dashboard area
    private static final Color DASH_BACKGROUND   = new Color(15, 23, 42);   
    private static final Color DASH_MUTED_TEXT   = new Color(148, 163, 184); 
    private static final Color DASH_POSITIVE_TEXT = new Color(34, 197, 94);  

    private JTable teamTable;
    private JTable playerTable;
    private JTable performanceTable;

    private DefaultTableModel teamTableModel;
    private DefaultTableModel playerTableModel;
    private DefaultTableModel performanceTableModel;

    // Quick stats data
    private JLabel rankValueLabel;
    private JLabel rankSubLabel;
    private JLabel winPctValueLabel;
    private JLabel winPctSubLabel;
    private JLabel streakValueLabel;
    private JLabel streakSubLabel;
    private JLabel avgPtsValueLabel;
    private JLabel avgPtsSubLabel;

    // Constructor
    public FanPanel(CardLayout cardLayout, JPanel cards) {
        // Pass layout + cards up to BasePanel
        super("Fan Page", cardLayout, cards);

        // Column layout for the whole panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppColors.darkTeal);

        // Small top spacer 
        add(Box.createRigidArea(new Dimension(0, 10)));

        // LOGIN BOX (State 1)
        loginBox = new JPanel(new GridBagLayout());  // center the card
        loginBox.setBackground(AppColors.darkTeal);
        loginBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        // White card that holds image + form side-by-side
        JPanel loginCard = new JPanel(new BorderLayout(24, 0));
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        loginCard.setPreferredSize(new Dimension(900, 340));

        // LEFT SIDE: image + fan info 
        JPanel leftPanel = new JPanel(new BorderLayout(0, 12));
        leftPanel.setBackground(Color.WHITE);

        // Image (adjust path and size as needed)
        ImageIcon rawIcon = new ImageIcon(
                getClass().getResource("/Resources/images/fans.jpg"));   // pulls file from classpath
        Image scaled = rawIcon.getImage().getScaledInstance(340, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Info about what fans can do
        JLabel infoLabel = new JLabel(
            "<html><b>As a CSU fan, you can:</b><br>" +
            "• View CCAA standings <br>" +
            "• Track win/loss trends for the Cougars<br>" +
            "• See top player performance stats<br>" +
            "• Compare CCAA Team Stats" +
            "</html>"
        );
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        leftPanel.add(imageLabel, BorderLayout.CENTER);
        leftPanel.add(infoLabel, BorderLayout.SOUTH);

        // RIGHT SIDE: login form
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // Title
        JLabel loginTitle = new JLabel("Fan Login");
        loginTitle.setFont(loginTitle.getFont().deriveFont(Font.BOLD, 22f));
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(loginTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        // Email row 
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));
        emailPanel.setBackground(Color.WHITE);

        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setForeground(Color.BLACK);

        emailField = new JTextField(30);
        emailField.setMaximumSize(new Dimension(400, 25));

        emailPanel.add(emailLabel);
        emailPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        emailPanel.add(emailField);
        emailPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(emailPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password row 
        JPanel passPanel = new JPanel();
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.X_AXIS));
        passPanel.setBackground(Color.WHITE);

        JLabel passLabel = new JLabel("Password: ");
        passLabel.setForeground(Color.BLACK);

        passwordField = new JPasswordField(30);
        passwordField.setMaximumSize(new Dimension(400, 25));

        passPanel.add(passLabel);
        passPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        passPanel.add(passwordField);
        passPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(passPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Message label (for errors)
        messageLabel = new JLabel(" ");
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageLabel.setForeground(Color.RED); // stays red on error
        rightPanel.add(messageLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Buttons row (Login + Back) 
        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.setBackground(Color.WHITE);

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back to Main");

        loginButton.addActionListener(e -> handleLogin());
        backButton.addActionListener(e -> cardLayout.show(cards, "Home Page"));

        buttonRow.add(loginButton);
        buttonRow.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonRow.add(backButton);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(buttonRow);
        rightPanel.add(Box.createVerticalGlue());

        // Put left and right into card
        loginCard.add(leftPanel, BorderLayout.WEST);
        loginCard.add(rightPanel, BorderLayout.CENTER);

        // Center the card on the blue background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginBox.add(loginCard, gbc);

        // Add login box to main panel
        add(loginBox);
        add(Box.createRigidArea(new Dimension(0, 20)));


        // DASHBOARD BOX (State 2) 
        // Initially hidden, shown after login
        dashboardBox = new JPanel();
        dashboardBox.setLayout(new BoxLayout(dashboardBox, BoxLayout.Y_AXIS));
        dashboardBox.setBackground(AppColors.darkTeal);
        dashboardBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Welcome label
        dashWelcomeLabel = new JLabel("Welcome, Fan!");
        dashWelcomeLabel.setForeground(Color.WHITE);
        dashWelcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashWelcomeLabel.setFont(dashWelcomeLabel.getFont().deriveFont(Font.BOLD, 20f));

        dashboardBox.add(Box.createRigidArea(new Dimension(0, 10)));
        dashboardBox.add(dashWelcomeLabel);
        dashboardBox.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel dashSubtitle = new JLabel("Track favorite teams, participate in polls, and view stats");
        dashSubtitle.setForeground(Color.WHITE);
        dashSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        dashboardBox.add(dashSubtitle);
        dashboardBox.add(Box.createRigidArea(new Dimension(0, 20)));

        // Actual dashboard content (quick stats + tabs)
        JPanel innerDashboard = createDashboardPanel();  
        innerDashboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        innerDashboard.setPreferredSize(new Dimension(950, 550));

        dashboardBox.add(innerDashboard);

        // Initially hidden until login success
        dashboardBox.setVisible(false);

        add(dashboardBox);
        add(Box.createVerticalGlue());
    }

    // Login Handling
    private void handleLogin() {
        String email = emailField.getText().trim();
        String pwd   = new String(passwordField.getPassword());

        try {
            User u = auth.signIn(email, pwd);

            // Enforce that ONLY FAN accounts work here
            if (u.role != Role.FAN) {
                showError("This account is not a Fan account.");
                return;
            }

            // Switch from login state → dashboard state
            dashWelcomeLabel.setText("Welcome, " + u.email + " (Fan)");

            // Load dashboard data from DB
            loadDashboardData();

            loginBox.setVisible(false);
            dashboardBox.setVisible(true);
            revalidate();
            repaint();

        } catch (Exception ex) {
            // "Invalid email or password"
            showError(ex.getMessage());
        }
    }

    private void showError(String msg) {
        messageLabel.setForeground(Color.RED);
        messageLabel.setText(msg);
    }

    private void handleLogout() {
        // Clear login fields and error message
        if (emailField != null) {
            emailField.setText("");
        }
        if (passwordField != null) {
            passwordField.setText("");
        }
        if (messageLabel != null) {
            messageLabel.setText(" ");
        }

        // Reset view states inside this panel
        if (loginBox != null) {
            loginBox.setVisible(true);
        }
        if (dashboardBox != null) {
            dashboardBox.setVisible(false);
        }

        // Switch back to the main/home panel in the CardLayout
        cardLayout.show(cards, "Home Page");
    }



    //  DASHBOARD UI 
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DASH_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        panel.add(createDashboardHeaderPanel(), BorderLayout.NORTH);
        panel.add(createDashboardMainContentPanel(), BorderLayout.CENTER);

        return panel;
    }

    // Pulls data from DB and fills dashboard tables
    private void loadDashboardData() {
        // First load standings + top players
        try {
            // 1. Standings summary (CCAA table)
            Object[][] standingsRows = statsDAO.fetchStandingsRows();
            setStandingsData(standingsRows);

            // 2. Top players for a hard-coded season for now
            Object[][] topPlayersRows = statsDAO.fetchTopPlayersRows("2024-25");
            setTopPlayersData(topPlayersRows);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load standings or top players.");
        }

        // 3. Performance by month 
        try {
            Object[][] performanceRows = statsDAO.fetchPerformanceRows();
            setPerformanceData(performanceRows);
        } catch (Exception e) {
            e.printStackTrace();
            // leave performance tab empty for now
        }

        // 4. Quick stat cards at top 
        updateQuickStats();
    }

    // HEADER 
    private JPanel createDashboardHeaderPanel() {

        // Outer header with left (titles) + right (logout button)
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBorder(BorderFactory.createEmptyBorder(4, 0, 12, 0));
        outer.setOpaque(false);

        // Left side: title + subtitle stacked vertically
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);


        JLabel title = new JLabel("Fan Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Track your favorite teams and players");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(DASH_MUTED_TEXT);

        left.add(title);
        left.add(Box.createVerticalStrut(4));
        left.add(subtitle);

        // Right side: Polls + Logout button
        JButton pollsButton = new JButton("Polls");
        pollsButton.addActionListener(e -> cardLayout.show(cards, "Polls Page"));


        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.add(pollsButton);
        right.add(Box.createHorizontalStrut(8));
        right.add(logoutButton);

        outer.add(left, BorderLayout.WEST);
        outer.add(right, BorderLayout.EAST);

        return outer;
    }

    // MAIN CONTENT (STATS + TABS)
    private JPanel createDashboardMainContentPanel() {
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        main.setOpaque(false);

        // Top: quick stats
        main.add(createQuickStatsPanel(), BorderLayout.NORTH);

        // Center: tabbed panels (Standings, Top Players, Performance)
        main.add(createTabs(), BorderLayout.CENTER);

        return main;
    }

    // QUICK STATS CARDS 
    private JPanel createQuickStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 12, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        statsPanel.setOpaque(false);

        // Favorite Team Rank
        rankValueLabel = new JLabel("#1");                // placeholder
        rankSubLabel   = new JLabel("2024–25 rank");
        JPanel rankCard = createStatCard("Favorite Team Rank",
                                        rankValueLabel,
                                        rankSubLabel);
        statsPanel.add(rankCard);

        // Win Percentage
        winPctValueLabel = new JLabel("0.0%");
        winPctSubLabel   = new JLabel("Conference record");
        JPanel winCard = createStatCard("Win Percentage",
                                        winPctValueLabel,
                                        winPctSubLabel);
        statsPanel.add(winCard);

        // Current Streak 
        streakValueLabel = new JLabel("-");
        streakSubLabel   = new JLabel("Current streak");
        JPanel streakCard = createStatCard("Current Streak",
                                        streakValueLabel,
                                        streakSubLabel);
        statsPanel.add(streakCard);

        // Avg Points/Game 
        avgPtsValueLabel = new JLabel("0.0");
        avgPtsSubLabel   = new JLabel("Avg points per game");
        JPanel ptsCard = createStatCard("Avg Points/Game",
                                        avgPtsValueLabel,
                                        avgPtsSubLabel);
        statsPanel.add(ptsCard);

        return statsPanel;
    }

    private void updateQuickStats() {
        try {
            final String TEAM_NAME = "Cal State San Marcos";

            // Use the standings table model (already filled by loadDashboardData)
            if (teamTableModel != null) {
                int rowCount = teamTableModel.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    Object nameObj = teamTableModel.getValueAt(i, 1); // column 1 = Team
                    if (nameObj != null) {
                        String rowName = nameObj.toString().trim();
                        if (rowName.equalsIgnoreCase(TEAM_NAME)) {

                            // Column mapping:
                            // 0 = "#", 1 = Team, 2 = Conf W, 3 = Conf L,
                            // 4 = Conf Win %, 5 = Overall, 6 = Home, 7 = Away, 8 = Neutral, 9 = Streak
                            Object rankObj    = teamTableModel.getValueAt(i, 0);
                            Object confWObj   = teamTableModel.getValueAt(i, 2);
                            Object confLObj   = teamTableModel.getValueAt(i, 3);
                            Object winPctObj  = teamTableModel.getValueAt(i, 4);
                            Object streakObj  = teamTableModel.getValueAt(i, 9);

                            // Favorite Team Rank
                            String rankText = (rankObj != null) ? rankObj.toString() : "?";
                            rankValueLabel.setText("#" + rankText);
                            rankSubLabel.setText("2024–2025 CCAA rank");

                            // Win Percentage (conference)
                            String confWText  = (confWObj != null) ? confWObj.toString() : "0";
                            String confLText  = (confLObj != null) ? confLObj.toString() : "0";
                            String winPctText = (winPctObj != null) ? winPctObj.toString() : "0.0";

                            winPctValueLabel.setText(winPctText + "%");
                            winPctSubLabel.setText(
                                    String.format("Conf record %s–%s", confWText, confLText)
                            );

                            // Current Streak
                            String streakText = (streakObj != null) ? streakObj.toString() : "-";
                            streakValueLabel.setText(streakText);
                            streakSubLabel.setText("Current conference streak");

                            break; // done once we find our team
                        }
                    }
                }
            }

            // Avg Points/Game from games table (may be 0.0 if no games loaded)
            double avgPts = statsDAO.fetchAvgPointsPerGame();
            avgPtsValueLabel.setText(String.format("%.1f", avgPts));
            avgPtsSubLabel.setText("Avg points per game");

        } catch (Exception e) {
            e.printStackTrace();
            // If something fails, don't crash UI
        }
    }

    private JPanel createStatCard(String label, JLabel valueLbl, JLabel subLbl) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setBackground(new Color(15, 23, 42)); // dark background for cards

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelLbl.setForeground(DASH_MUTED_TEXT);

        valueLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueLbl.setForeground(Color.WHITE);

        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subLbl.setForeground(DASH_POSITIVE_TEXT);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(labelLbl, BorderLayout.NORTH);
        top.add(valueLbl, BorderLayout.CENTER);

        card.add(top, BorderLayout.CENTER);
        card.add(subLbl, BorderLayout.SOUTH);

        return card;
    }

    // TABS
    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabs.setBackground(DASH_BACKGROUND);
        tabs.setForeground(Color.WHITE);
        tabs.setOpaque(true);
        tabs.setBorder(BorderFactory.createEmptyBorder());

        tabs.addTab("Standings", createStandingsPanel());
        tabs.addTab("Top Players", createTopPlayersPanel());
        tabs.addTab("Performance", createPerformancePanel());

        return tabs;
    }

    // STANDINGS TAB
    private JPanel createStandingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("California Collegiate Athletic Association Standings");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        JLabel desc = new JLabel("2024-2025 season standings");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        desc.setForeground(DASH_MUTED_TEXT);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        header.add(title);
        header.add(desc);

        String[] columns = {
            "#",
            "Team",
            "Conf W",
            "Conf L",
            "Conf Win %",
            "Overall",
            "Home",
            "Away",
            "Neutral",
            "Streak"
        };

        // Start with an empty model, real data is loaded from the database
        teamTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        teamTable = new JTable(teamTableModel);
        teamTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = createTableScrollPane(teamTable);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        teamTable.getColumnModel().getColumn(1).setPreferredWidth(180);

        return panel;
    }

    // TOP PLAYERS TAB 
    private JPanel createTopPlayersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Top Players");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        JLabel desc = new JLabel("Leading performers this season");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        desc.setForeground(DASH_MUTED_TEXT);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        header.add(title);
        header.add(desc);

        String[] columns = {"#", "Player", "Team", "PPG", "RPG", "APG", "Rating"};

        // Start with an empty model; real data is loaded from the database
        playerTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        playerTable = new JTable(playerTableModel);
        playerTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = createTableScrollPane(playerTable);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // PERFORMANCE TAB 
    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Team Performance Over Time");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        JLabel desc = new JLabel("Monthly points and wins (charts can be added later)");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        desc.setForeground(DASH_MUTED_TEXT);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        header.add(title);
        header.add(desc);

        String[] columns = {"Month", "Points", "Wins"};

        // Start with an empty model; real data is loaded from the database
        performanceTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        performanceTable = new JTable(performanceTableModel);
        performanceTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = createTableScrollPane(performanceTable);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    //  PUBLIC HOOKS for  DB integration
    public void setStandingsData(Object[][] rows) {
        replaceTableData(teamTableModel, rows);
    }

    public void setTopPlayersData(Object[][] rows) {
        replaceTableData(playerTableModel, rows);
    }

    public void setPerformanceData(Object[][] rows) {
        replaceTableData(performanceTableModel, rows);
    }

    //  Helper methods for tables
    private JScrollPane createTableScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(DASH_BACKGROUND);
        scrollPane.setOpaque(false);
        return scrollPane;
    }

    private void replaceTableData(DefaultTableModel model, Object[][] rows) {
        if (model == null) return;

        model.setRowCount(0); // clear old rows
        if (rows == null) return;

        for (Object[] row : rows) {
            model.addRow(row);
        }
    }
}
