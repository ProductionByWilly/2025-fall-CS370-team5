import Database.AuthService;
import Database.User;
import java.awt.*;
import javax.swing.*;

public class AuthTestWindow {

    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private final AuthService auth = new AuthService();

    public AuthTestWindow() {
        frame = new JFrame("Auth Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0; gbc.gridy = 0;
        frame.add(emailLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        frame.add(emailField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 1;
        frame.add(passLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        frame.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 1; gbc.gridy = 2;
        frame.add(loginButton, gbc);

        messageLabel = new JLabel(" ");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        frame.add(messageLabel, gbc);

        loginButton.addActionListener(e -> handleLogin());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String pwd = new String(passwordField.getPassword());

        try {
            User u = auth.signIn(email, pwd);
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText("Success: " + u.role + " → " + u.email);
        } catch (Exception ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AuthTestWindow::new);
    }
}
