import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FanDashboardPanel extends JPanel {

    private static final Color BACKGROUND = new Color(15, 23, 42);
    private static final Color MUTED_TEXT = new Color(148, 163, 184);
    private static final Color POSITIVE_TEXT = new Color(34, 197, 94);

    private JTable teamTable;
    private JTable playerTable;
    private JTable performanceTable;
    private DefaultTableModel teamTableModel;
    private DefaultTableModel playerTableModel;
    private DefaultTableModel performanceTableModel;

    public FanDashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND); // similar to bg-slate-950

        // Header at the top
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main content with quick stats + tabs
        add(createMainContentPanel(), BorderLayout.CENTER);
    }

    // ---------------- HEADER ----------------

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        header.setOpaque(false);

        JLabel title = new JLabel("Fan Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Track your favorite teams and players");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED_TEXT); // slate-400-ish

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        return header;
    }

    // ------------- MAIN CONTENT (STATS + TABS) -------------

    private JPanel createMainContentPanel() {
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
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
        card.setBackground(new Color(15, 23, 42)); // dark background for cards

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelLbl.setForeground(MUTED_TEXT);

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel(subtext);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subLbl.setForeground(POSITIVE_TEXT); // green-ish, like "TrendingUp"

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(labelLbl, BorderLayout.NORTH);
        top.add(valueLbl, BorderLayout.CENTER);

        card.add(top, BorderLayout.CENTER);
        card.add(subLbl, BorderLayout.SOUTH);

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

        tabs.addTab("Standings", createStandingsPanel());
        tabs.addTab("Top Players", createTopPlayersPanel());
        tabs.addTab("Performance", createPerformancePanel());

        return tabs;
    }

    // ------------- STANDINGS TAB ----------------

    private JPanel createStandingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Western Conference Standings");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        JLabel desc = new JLabel("Current season standings");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        desc.setForeground(MUTED_TEXT);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        header.add(title);
        header.add(desc);

        String[] columns = {
        "Month",
        "Avg Points",
        "W-L",
        "Avg Margin",
        "High",
        "Low",
        "Opp Avg"
        };



        teamTable = new JTable(teamTableModel);
        teamTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = createTableScrollPane(teamTable);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ------------- TOP PLAYERS TAB ----------------

    private JPanel createTopPlayersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Top Players");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        JLabel desc = new JLabel("Leading performers this season");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        desc.setForeground(MUTED_TEXT);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        header.add(title);
        header.add(desc);

        String[] columns = {"#", "Player", "Team", "PPG", "RPG", "APG", "Rating"};
        Object[][] data = {
                {1, "Kevin Durant", "Phoenix Suns", 28.5, 7.2, 5.8, 95},
                {2, "LeBron James", "Lakers", 26.3, 8.1, 7.5, 94},
                {3, "Stephen Curry", "Warriors", 27.8, 5.2, 6.3, 93},
                {4, "Kawhi Leonard", "Clippers", 24.1, 6.8, 4.2, 91}
        };

        performanceTableModel = new DefaultTableModel(data, columns) {
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

    // ------------- PERFORMANCE TAB ----------------

    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Team Performance Over Time");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        JLabel desc = new JLabel("Monthly points and wins (charts can be added later)");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        desc.setForeground(MUTED_TEXT);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        header.add(title);
        header.add(desc);

        String[] columns = {"Month", "Points", "Wins"};
        Object[][] data = {
                {"Oct", 108, 8},
                {"Nov", 112, 10},
                {"Dec", 115, 12},
                {"Jan", 110, 9},
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

    // ------------- PUBLIC HOOKS (for later DB integration) -------------

    public JTable getTeamTable() {
        return teamTable;
    }

    public JTable getPlayerTable() {
        return playerTable;
    }

    public JTable getPerformanceTable() {
        return performanceTable;
    }

    public void setStandingsData(Object[][] rows) {
        replaceTableData(teamTableModel, rows);
    }

    public void setTopPlayersData(Object[][] rows) {
        replaceTableData(playerTableModel, rows);
    }

    public void setPerformanceData(Object[][] rows) {
        replaceTableData(performanceTableModel, rows);
    }

    private JScrollPane createTableScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.setOpaque(false);
        return scrollPane;
    }

    private void replaceTableData(DefaultTableModel model, Object[][] rows) {
        if (model == null) {
            return;
        }
        model.setRowCount(0);
        if (rows == null) {
            return;
        }
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }
}
