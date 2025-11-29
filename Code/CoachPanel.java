import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Database.Database;

public class CoachPanel extends BasePanel {
    private static final Color BACKGROUND = new Color(15, 23, 42);
    private static final Color MUTED_TEXT = new Color(148, 163, 184);
    private static final Color POSITIVE_TEXT = new Color(34, 197, 94);
    // Quick stat cards
    private JLabel teamRecordValueLabel;
    private JLabel winRateValueLabel;
    private JLabel activePlayersValueLabel;
    private JLabel nextGameValueLabel;

    public CoachPanel(CardLayout cardLayout, JPanel cards) {
        super("Coach Dashboard", cardLayout, cards); // <- required by BasePanel
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        // Header
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main content: quick stats + tabs
        add(createMainContentPanel(), BorderLayout.CENTER);

        // Load DB data
        loadDashboardData();
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
        JPanel nextGameCard = createStatCard("Next Game", nextGameValueLabel = new JLabel("Loading..."), "");

        // Add cards to stats panel
        statsPanel.add(teamRecordCard);
        statsPanel.add(winRateCard);
        statsPanel.add(activePlayersCard);
        statsPanel.add(nextGameCard);

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
        tabs.addTab("Schedule", createSchedulePanel());

        return tabs;
    }

    private JPanel createRosterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columns = {"Player", "Position", "Status"};
        Object[][] data = {}; // populate from DB later
        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(BACKGROUND);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTeamStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columns = {"Metric", "Value"};
        Object[][] data = {
                {"Wins", "0"},
                {"Losses", "0"},
                {"Win Rate", "0%"},
                {"Active Players", "0"}
        };
        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(BACKGROUND);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columns = {"Date", "Opponent", "Result"};
        Object[][] data = {}; // populate from DB later
        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(BACKGROUND);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ---------------- LOAD DATA ----------------
    private void loadDashboardData() {
        try (Connection conn = Database.getConnection()) {
            // Team record and win rate
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

            // Active players
            String activeSQL = "SELECT COUNT(*) AS total FROM team";
            try (PreparedStatement st = conn.prepareStatement(activeSQL);
                 ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    activePlayersValueLabel.setText(String.valueOf(rs.getInt("total")));
                }
            }

            // Next game
            String nextSQL = """
                SELECT opponent_name, game_date 
                FROM games 
                WHERE game_date >= CURDATE()
                ORDER BY game_date ASC
                LIMIT 1
                """;
            try (PreparedStatement st = conn.prepareStatement(nextSQL);
                 ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    String nextGameStr = "vs " + rs.getString("opponent_name") + " on " + rs.getString("game_date");
                    nextGameValueLabel.setText(nextGameStr);
                } else {
                    nextGameValueLabel.setText("No upcoming games");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            teamRecordValueLabel.setText("ERROR");
            winRateValueLabel.setText("ERROR");
            activePlayersValueLabel.setText("ERROR");
            nextGameValueLabel.setText("ERROR");
        }
        }
}


