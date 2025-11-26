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

    // Constructor
    public FanPanel(CardLayout cardLayout, JPanel cards) {
        // Pass layout + cards up to BasePanel
        super("Fan Page", cardLayout, cards);

        // Column layout for the whole panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppColors.darkTeal);

        // Top Title for entire Panel
        JLabel title = new JLabel("Fan Dashboard");
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(title);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // LOGIN BOX (State 1)
        loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBackground(AppColors.darkTeal);
        loginBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Email row 
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));
        emailPanel.setBackground(AppColors.darkTeal);

        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setForeground(Color.WHITE);

        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(250, 25));

        emailPanel.add(emailLabel);
        emailPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        emailPanel.add(emailField);
        emailPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBox.add(emailPanel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password row 
        JPanel passPanel = new JPanel();
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.X_AXIS));
        passPanel.setBackground(AppColors.darkTeal);

        JLabel passLabel = new JLabel("Password: ");
        passLabel.setForeground(Color.WHITE);

        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(250, 25));

        passPanel.add(passLabel);
        passPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        passPanel.add(passwordField);
        passPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBox.add(passPanel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 15)));

        // Message label (for errors, etc.)
        messageLabel = new JLabel(" ");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setForeground(Color.WHITE);
        loginBox.add(messageLabel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 15)));

        // Buttons row (Login + Back) 
        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.setBackground(AppColors.darkTeal);

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back to Main");

        loginButton.addActionListener(e -> handleLogin());
        backButton.addActionListener(e -> cardLayout.show(cards, "Home Page"));

        buttonRow.add(loginButton);
        buttonRow.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonRow.add(backButton);
        buttonRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBox.add(buttonRow);

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

        // ---- Actual dashboard content (quick stats + tabs) ----
        JPanel innerDashboard = createDashboardPanel();   // <<<<<< this builds what used to be FanDashboardPanel
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
            loginBox.setVisible(false);
            dashboardBox.setVisible(true);
            revalidate();
            repaint();

        } catch (Exception ex) {
            // "Invalid email or password", etc.
            showError(ex.getMessage());
        }
    }

    private void showError(String msg) {
        messageLabel.setForeground(Color.RED);
        messageLabel.setText(msg);
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

    // HEADER 
    private JPanel createDashboardHeaderPanel() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(4, 0, 12, 0));
        header.setOpaque(false);

        JLabel title = new JLabel("Fan Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Track your favorite teams and players");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(DASH_MUTED_TEXT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        return header;
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

        statsPanel.add(createStatCard("Favorite Team Rank", "#1", "Up 2 positions"));
        statsPanel.add(createStatCard("Win Percentage", "71.4%", "+3.2% this month"));
        statsPanel.add(createStatCard("Current Streak", "W5", "5 wins in a row"));
        statsPanel.add(createStatCard("Avg Points/Game", "118.2", "League leading"));

        return statsPanel;
    }

        private JPanel createStatCard(String label, String value, String subtext) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setBackground(new Color(15, 23, 42)); // slightly darker than outer

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelLbl.setForeground(DASH_MUTED_TEXT);

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel(subtext);
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

        JLabel title = new JLabel("Western Conference Standings");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        JLabel desc = new JLabel("Current season standings");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        desc.setForeground(DASH_MUTED_TEXT);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        header.add(title);
        header.add(desc);

        String[] columns = {"#", "Team", "Wins", "Losses", "Win %", "Streak"};
        Object[][] data = {
                {1, "Phoenix Suns", 45, 18, "71.4", "W5"},
                {2, "Lakers",        42, 21, "66.7", "L2"},
                {3, "Warriors",      40, 23, "63.5", "W3"},
                {4, "Clippers",      38, 25, "60.3", "W1"}
        };

        teamTableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only
            }
        };

        teamTable = new JTable(teamTableModel);
        teamTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = createTableScrollPane(teamTable);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

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
        Object[][] data = {
                {1, "Kevin Durant",   "Phoenix Suns", 28.5, 7.2, 5.8, 95},
                {2, "LeBron James",   "Lakers",       26.3, 8.1, 7.5, 94},
                {3, "Stephen Curry",  "Warriors",     27.8, 5.2, 6.3, 93},
                {4, "Kawhi Leonard",  "Clippers",     24.1, 6.8, 4.2, 91}
        };

        playerTableModel = new DefaultTableModel(data, columns) {
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
        Object[][] data = {
                {"Oct", 108,  8},
                {"Nov", 112, 10},
                {"Dec", 115, 12},
                {"Jan", 110,  9},
                {"Feb", 118, 11},
                {"Mar", 120, 13}
        };

        performanceTableModel = new DefaultTableModel(data, columns) {
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


    //  PUBLIC HOOKS (for later DB integration)
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