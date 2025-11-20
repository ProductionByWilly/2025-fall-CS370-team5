import java.sql.*;
import java.util.Optional;
import Database.Database;

public class UserDAO {
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id,email,password_hash,role FROM users WHERE email=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        Role.valueOf(rs.getString("role"))
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public void insert(String email, String passwordHash, Role role) throws Exception {
    String sql = "INSERT INTO users (email, password_hash, role) VALUES (?, ?, ?)";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        stmt.setString(2, passwordHash);
        stmt.setString(3, role.name());

        stmt.executeUpdate();
    }
}
}
