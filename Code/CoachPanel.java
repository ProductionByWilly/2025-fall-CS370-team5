import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.TableRowSorter;
import Database.Database;
import Database.User;
import Database.Role;

public class CoachPanel extends BasePanel {
    private static final Color BACKGROUND = new Color(15, 23, 42);
    private static final Color MUTED_TEXT = new Color(148, 163, 184);
    private static final Color POSITIVE_TEXT = new Color(34, 197, 94);

    private DefaultTableModel rosterModel;
    private JTable rosterTable;

    // Login related fields
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    // Panels for login/dashboard
    private JPanel loginBox;
    private JPanel dashboardBox;
    private JLabel dashWelcomeLabel;

    // Auth service
    private final AuthService auth = new AuthService();

    // Quick stat cards
    private JLabel teamRecordValueLabel;
    private JLabel winRateValueLabel;
    private JLabel activePlayersValueLabel;
    private JLabel averagePointsValueLabel;

    public CoachPanel(CardLayout cardLayout, JPanel cards) {
        super("Coach Page", cardLayout, cards);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppColors.darkTeal);

        createLoginPanel();             // builds loginBox
        createDashboardPanelWrapper();  // builds dashboardBox
    }


    // ---------------- HEADER ----------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        header.setOpaque(false);

        JLabel title = new JLabel("Coach Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Track team performance and manage players");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED_TEXT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        return header;
    }

    // ---------------- MAIN CONTENT ----------------
    private JPanel createMainContentPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        main.setOpaque(false);

        // Top: quick stats
        main.add(createQuickStatsPanel(), BorderLayout.NORTH);

        // Center: tabbed panels
        main.add(createTabs(), BorderLayout.CENTER);

        return main;
    }

    // ---------------- QUICK STATS CARDS ----------------
    private JPanel createQuickStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 12, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        statsPanel.setOpaque(false);

        // Create each card
        JPanel teamRecordCard = createStatCard("Team Record", teamRecordValueLabel = new JLabel("Loading..."), "");
        JPanel winRateCard = createStatCard("Win Percentage", winRateValueLabel = new JLabel("Loading..."), "");
        JPanel activePlayersCard = createStatCard("Active Players", activePlayersValueLabel = new JLabel("Loading..."), "");
        JPanel averagePointsCard = createStatCard("Avg Points/Game", averagePointsValueLabel = new JLabel("Loading..."), "");

        // Add cards to stats panel
        statsPanel.add(teamRecordCard);
        statsPanel.add(winRateCard);
        statsPanel.add(activePlayersCard);
        statsPanel.add(averagePointsCard);

        return statsPanel;
    }

    /**
     * Generic card creation with label on top, value in middle, and optional subtext.
     * The valueLabel is passed in so we can update it later.
     */
    private JPanel createStatCard(String labelText, JLabel valueLabel, String subtextStr) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setBackground(new Color(24, 32, 52)); // dark card background

        // Top label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(MUTED_TEXT);

        // Value label (already passed in)
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueLabel.setForeground(Color.WHITE);

        // Optional subtext
        JLabel subtext = new JLabel(subtextStr);
        subtext.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtext.setForeground(POSITIVE_TEXT);

        // Assemble top panel
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(label, BorderLayout.NORTH);
        top.add(valueLabel, BorderLayout.CENTER);

        card.add(top, BorderLayout.CENTER);
        card.add(subtext, BorderLayout.SOUTH);

        return card;
    }

    // ---------------- TABS ----------------
    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabs.setBackground(BACKGROUND);
        tabs.setForeground(Color.WHITE);
        tabs.setOpaque(false);
        tabs.setBorder(BorderFactory.createEmptyBorder());

        tabs.addTab("Roster", createRosterPanel());
        tabs.addTab("Team Stats", createTeamStatsPanel());
        tabs.addTab("Standings", createStandingsPanel());

        return tabs;
    }

    private JPanel createRosterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columns = {
                "Jersey", "Player", "GP", "GS", "Minutes",
                "OREB", "DREB", "REB", "AST", "TO", "BLK", "STL", "PTS", "PF"
        };

        rosterModel = new DefaultTableModel(columns, 0);
        rosterTable = new JTable(rosterModel);

        // 🔥 ENABLE SORTING
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(rosterModel);
        rosterTable.setRowSorter(sorter);

        // Make numeric columns sort numerically
        int[] numericColumns = {0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        for (int col : numericColumns) {
            sorter.setComparator(col, (o1, o2) -> Integer.compare((Integer)o1, (Integer)o2));
        }

        JScrollPane scroll = new JScrollPane(rosterTable);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(BACKGROUND);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTeamStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columns = {"Metric", "Value"};
        Object[][] data = new Object[10][2]; // adjust rows as needed

        try (Connection conn = Database.getConnection()) {
            // ---------------- WINS / LOSSES ----------------
            String recordSQL = "SELECT " +
                    "SUM(CASE WHEN result='W' THEN 1 ELSE 0 END) AS wins, " +
                    "SUM(CASE WHEN result='L' THEN 1 ELSE 0 END) AS losses, " +
                    "COUNT(*) AS total_games " +
                    "FROM games";

            try (Statement st  = conn.createStatement();
                 ResultSet rs = st.executeQuery(recordSQL)) {
                if (rs.next()) {
                    int wins = rs.getInt("wins");
                    int losses = rs.getInt("losses");
                    int total = rs.getInt("total_games");

                    data[0][0] = "Wins";
                    data[0][1] = wins;
                    data[1][0] = "Losses";
                    data[1][1] = losses;
                    data[2][0] = "Win Rate";
                    data[2][1] = total > 0 ? String.format("%.1f%%", wins * 100.0 / total) : "0%";
                }
            }

            // ---------------- PLAYER-BASED STATS ----------------
            String statsSQL = "SELECT " +
                    "SUM(pts) AS total_pts, " +
                    "SUM(ast) AS total_ast, " +
                    "SUM(`to`) AS total_to, " +
                    "SUM(blk) AS total_blk, " +
                    "SUM(stl) AS total_stl, " +
                    "SUM(pf) AS total_pf, " +
                    "SUM(reb) AS total_reb " +
                    "FROM player_season_stats " +
                    "WHERE season='2024-25'";

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(statsSQL)) {
                if (rs.next()) {
                    int totalPts = rs.getInt("total_pts");
                    int totalAst = rs.getInt("total_ast");
                    int totalTo = rs.getInt("total_to");
                    int totalBlk = rs.getInt("total_blk");
                    int totalStl = rs.getInt("total_stl");
                    int totalPf = rs.getInt("total_pf");
                    int totalReb = rs.getInt("total_reb");

                    // Number of games (from games table)
                    int totalGames = 0;
                    try (ResultSet rs2 = conn.createStatement().executeQuery("SELECT COUNT(*) AS cnt FROM games")) {
                        if (rs2.next()) totalGames = rs2.getInt("cnt");
                    }

                    data[3][0] = "Avg Points/Game";
                    data[3][1] = totalGames > 0 ? String.format("%.1f", totalPts * 1.0 / totalGames) : "0";
                    data[4][0] = "Avg Assists/Game";
                    data[4][1] = totalGames > 0 ? String.format("%.1f", totalAst * 1.0 / totalGames) : "0";
                    data[5][0] = "Avg Turnovers/Game";
                    data[5][1] = totalGames > 0 ? String.format("%.1f", totalTo * 1.0 / totalGames) : "0";
                    data[6][0] = "Avg Blocks/Game";
                    data[6][1] = totalGames > 0 ? String.format("%.1f", totalBlk * 1.0 / totalGames) : "0";
                    data[7][0] = "Avg Steals/Game";
                    data[7][1] = totalGames > 0 ? String.format("%.1f", totalStl * 1.0 / totalGames) : "0";
                    data[8][0] = "Avg Fouls/Game";
                    data[8][1] = totalGames > 0 ? String.format("%.1f", totalPf * 1.0 / totalGames) : "0";
                    data[9][0] = "Avg Rebounds/Game";
                    data[9][1] = totalGames > 0 ? String.format("%.1f", totalReb * 1.0 / totalGames) : "0";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            for (int i = 0; i < data.length; i++) {
                data[i][1] = "ERROR";
            }
        }

        JTable table = new JTable(data, columns);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(BACKGROUND);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStandingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columns = {
                "Rank", "Team", "Conf Wins", "Conf Losses", "Conf %",
                "Overall Wins", "Overall Losses", "Overall %",
                "Home W-L", "Away W-L", "Neutral W-L", "Streak"
        };

        java.util.List<Object[]> dataList = new java.util.ArrayList<>();

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM mbb_standings_2024_25 ORDER BY conf_pct DESC")) {

            int rank = 1;
            while (rs.next()) {
                Object[] row = new Object[columns.length];
                row[0] = rank++;
                row[1] = rs.getString("team_name");
                row[2] = rs.getInt("conf_wins");
                row[3] = rs.getInt("conf_losses");
                row[4] = String.format("%.3f", rs.getDouble("conf_pct"));
                row[5] = rs.getInt("overall_wins");
                row[6] = rs.getInt("overall_losses");
                row[7] = String.format("%.3f", rs.getDouble("overall_pct"));
                row[8] = rs.getInt("home_wins") + "-" + rs.getInt("home_losses");
                row[9] = rs.getInt("away_wins") + "-" + rs.getInt("away_losses");
                row[10] = rs.getInt("neutral_wins") + "-" + rs.getInt("neutral_losses");
                row[11] = rs.getString("streak");

                dataList.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convert List to array for JTable
        Object[][] data = dataList.toArray(new Object[0][]);

        // Create table model
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table read-only
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // For numeric sorting on columns like wins/losses
                switch (columnIndex) {
                    case 0: case 2: case 3: case 5: case 6: return Integer.class;
                    default: return String.class;
                }
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);

        // Make table sortable
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(BACKGROUND);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ---------------- LOAD DATA ----------------
    private void loadDashboardData() {
        try (Connection conn = Database.getConnection()) {
            // ---------------- TEAM RECORD & WIN RATE ----------------
            String recordSQL = """
                SELECT 
                    SUM(CASE WHEN result='W' THEN 1 ELSE 0 END) AS wins,
                    SUM(CASE WHEN result='L' THEN 1 ELSE 0 END) AS losses,
                    COUNT(*) AS total
                FROM games
                """;
            try (PreparedStatement st = conn.prepareStatement(recordSQL);
                 ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    int wins = rs.getInt("wins");
                    int losses = rs.getInt("losses");
                    int total = rs.getInt("total");

                    // <-- update the new value labels here
                    teamRecordValueLabel.setText(wins + " - " + losses);
                    winRateValueLabel.setText(total > 0 ? String.format("%.1f%%", wins * 100.0 / total) : "0%");
                }
            }

            // ---------------- LOAD ROSTER DATA ----------------
            String rosterSQL = """
    SELECT jersey, player_name, gp, gs, min, oreb, dreb, reb, ast, `to`, blk, stl, pts, pf
    FROM player_season_stats
    ORDER BY jersey ASC
""";

            try (PreparedStatement st = conn.prepareStatement(rosterSQL);
                 ResultSet rs = st.executeQuery()) {

                rosterModel.setRowCount(0); // clear old data

                while (rs.next()) {
                    rosterModel.addRow(new Object[]{
                            rs.getInt("jersey"),
                            rs.getString("player_name"),
                            rs.getInt("gp"),
                            rs.getInt("gs"),
                            rs.getInt("min"),
                            rs.getInt("oreb"),
                            rs.getInt("dreb"),
                            rs.getInt("reb"),
                            rs.getInt("ast"),
                            rs.getInt("to"),
                            rs.getInt("blk"),
                            rs.getInt("stl"),
                            rs.getInt("pts"),
                            rs.getInt("pf")
                    });
                }
            }

            // Update "Active Players" card with roster count
            activePlayersValueLabel.setText(String.valueOf(rosterModel.getRowCount()));

            // ---------------- AVERAGE POINTS PER GAME ----------------
            String avgPointsSQL = "SELECT AVG(team_score) AS avg_points FROM games";

            try (PreparedStatement st = conn.prepareStatement(avgPointsSQL);
                 ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    double avgPoints = rs.getDouble("avg_points");
                    averagePointsValueLabel.setText(String.format("%.1f", avgPoints));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            teamRecordValueLabel.setText("ERROR");
            winRateValueLabel.setText("ERROR");
            activePlayersValueLabel.setText("ERROR");
            averagePointsValueLabel.setText("ERROR");
        }
        }
    // ---------------- CREATE LOGIN PANEL ----------------
    private void createLoginPanel() {
        loginBox = new JPanel(new GridBagLayout());
        loginBox.setBackground(AppColors.darkTeal);
        loginBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel loginCard = new JPanel(new BorderLayout(24, 0));
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        loginCard.setPreferredSize(new Dimension(900, 340));

        // Left: info
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        JLabel infoLabel = new JLabel("<html><b>As a CSU coach, you can:</b><br>" +
                "• Manage teams<br>• View analytics<br>• Track player stats</html>");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        leftPanel.add(infoLabel, BorderLayout.CENTER);

        // Right: login form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);

        JLabel loginTitle = new JLabel("Coach Login");
        loginTitle.setFont(loginTitle.getFont().deriveFont(Font.BOLD, 22f));
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(loginTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Email
        emailField = new JTextField(30);
        emailField.setMaximumSize(new Dimension(400, 25));
        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setForeground(Color.BLACK);
        rightPanel.add(emailLabel);
        rightPanel.add(emailField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password
        passwordField = new JPasswordField(30);
        passwordField.setMaximumSize(new Dimension(400, 25));
        JLabel passLabel = new JLabel("Password: ");
        passLabel.setForeground(Color.BLACK);
        rightPanel.add(passLabel);
        rightPanel.add(passwordField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        rightPanel.add(messageLabel);

        // Buttons
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back to Main");
        loginButton.addActionListener(e -> handleLogin());
        backButton.addActionListener(e -> cardLayout.show(cards, "Home Page"));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonRow.add(loginButton);
        buttonRow.add(backButton);
        rightPanel.add(buttonRow);

        loginCard.add(leftPanel, BorderLayout.WEST);
        loginCard.add(rightPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginBox.add(loginCard, gbc);

        add(loginBox);
    }
    private void createDashboardPanelWrapper() {
        dashboardBox = new JPanel(new BorderLayout());
        dashboardBox.setBackground(AppColors.darkTeal);
        dashboardBox.setVisible(false);

        // Top panel with welcome label + logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        dashWelcomeLabel = new JLabel("Welcome, Coach!");
        dashWelcomeLabel.setForeground(Color.WHITE);
        dashWelcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        dashWelcomeLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> handleLogout());

        topPanel.add(dashWelcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);

        dashboardBox.add(topPanel, BorderLayout.NORTH);

        // Full dashboard content
        JPanel fullDashboard = new JPanel(new BorderLayout());
        fullDashboard.setOpaque(false);
        fullDashboard.add(createHeaderPanel(), BorderLayout.NORTH);
        fullDashboard.add(createMainContentPanel(), BorderLayout.CENTER);

        dashboardBox.add(fullDashboard, BorderLayout.CENTER);

        add(dashboardBox);
    }
    private void handleLogin() {
        String email = emailField.getText().trim();
        String pwd = new String(passwordField.getPassword());

        try {
            User u = auth.signIn(email, pwd);

            if (u.role != Role.COACH) {
                messageLabel.setText("This account is not a Coach account."); // <-- fix here
                return;
            }

            dashWelcomeLabel.setText("Welcome, " + u.email + " (Coach)");

            // Load live stats after login
            loadDashboardData();

            loginBox.setVisible(false);
            dashboardBox.setVisible(true);

            revalidate();
            repaint();

        } catch (Exception ex) {
            messageLabel.setText("Invalid email or password");
        }
    }
    private void handleLogout() {
        emailField.setText("");
        passwordField.setText("");
        messageLabel.setText(" ");

        dashboardBox.setVisible(false);
        loginBox.setVisible(true);
    }
}