import java.sql.*;
import java.util.Optional;

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
}
