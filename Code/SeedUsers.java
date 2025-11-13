import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class SeedUsers {
    public static void main(String[] args) throws Exception {
        try (Connection c = Database.getConnection()) {
            insert(c, "fan@demo.com",    "password123", "FAN");
            insert(c, "player@demo.com", "password123", "PLAYER");
            insert(c, "coach@demo.com",  "password123", "COACH");
            System.out.println("✅ Seeded users (ignored if already exist).");
        }
    }
    private static void insert(Connection c, String email, String raw, String role) throws Exception {
        String hash = BCrypt.hashpw(raw, BCrypt.gensalt(12));
        try (PreparedStatement ps = c.prepareStatement(
            "INSERT IGNORE INTO users(email,password_hash,role) VALUES(?,?,?)")) {
            ps.setString(1, email.toLowerCase());
            ps.setString(2, hash);
            ps.setString(3, role);
            ps.executeUpdate();
        }
    }
}
