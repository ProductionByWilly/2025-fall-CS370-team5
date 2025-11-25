import java.awt.*;
import javax.swing.*;

public class FanPanel extends BasePanel {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    private JPanel loginBox;       // holds login UI
    private JPanel dashboardBox;   // holds dashboard UI
    private JLabel dashWelcomeLabel;

    // Uses your existing auth backend
    private final AuthService auth = new AuthService();

    public FanPanel(CardLayout cardLayout, JPanel cards) {
        // Pass layout + cards up to BasePanel
        super("Fan Page", cardLayout, cards);

        // Column layout for the whole panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppColors.darkTeal);

        // ===== Title =====
        JLabel title = new JLabel("Fan Dashboard");
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(title);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // =========================
        // LOGIN BOX (State 1)
        // =========================
        loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBackground(AppColors.darkTeal);
        loginBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ----- Email row -----
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

        // ----- Password row -----
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

        // ----- Message label -----
        messageLabel = new JLabel(" ");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBox.add(messageLabel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 15)));

        // ----- Buttons row (Login + Back) -----
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

        // =========================
        // DASHBOARD BOX (State 2)
        // =========================
        dashboardBox = new JPanel();
        dashboardBox.setLayout(new BoxLayout(dashboardBox, BoxLayout.Y_AXIS));
        dashboardBox.setBackground(AppColors.darkTeal);
        dashboardBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        dashWelcomeLabel = new JLabel("Welcome, Fan!");
        dashWelcomeLabel.setForeground(Color.WHITE);
        dashWelcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashWelcomeLabel.setFont(dashWelcomeLabel.getFont().deriveFont(Font.BOLD, 20f));

        dashboardBox.add(dashWelcomeLabel);
        dashboardBox.add(Box.createRigidArea(new Dimension(0, 20)));

        // TODO: add real dashboard content here later
        JLabel placeholder = new JLabel("Fan stats, favorite teams, and game history will go here.");
        placeholder.setForeground(Color.WHITE);
        placeholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashboardBox.add(placeholder);

        // Initially hidden until login success
        dashboardBox.setVisible(false);

        add(dashboardBox);
        add(Box.createVerticalGlue());
    }

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
}