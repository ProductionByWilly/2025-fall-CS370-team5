import javax.swing.*;
import java.awt.*;

public class FanDashboardPanelTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Fan Dashboard Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            FanDashboardPanel panel = new FanDashboardPanel();
            panel.setPreferredSize(new Dimension(1100, 700));

            frame.setContentPane(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
