//Leaderboard Panel Class
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {

    private LeaderboardService service;
    private JTable table;

    public LeaderboardPanel() {
        this.service = new LeaderboardService();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        table = new JTable();
        loadData();

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {
        try {
            List<PlayerRecord> data = service.getLeaderboard();
            String[] cols = {"Player", "Score"};

            DefaultTableModel model = new DefaultTableModel(cols, 0);

            for (PlayerRecord p : data) {
                model.addRow(new Object[]{p.getName(), p.getScore()});
            }

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}