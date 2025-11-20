import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;

public class AuthService {
    private final UserDAO dao = new UserDAO();

    public User signIn(String email, String rawPassword) {
        Optional<User> opt = dao.findByEmail(email);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        User u = opt.get();
        if (!BCrypt.checkpw(rawPassword, u.passwordHash)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return u;
    }

    public User signUp(String email, String rawPassword, Role role) throws Exception {
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (rawPassword == null || rawPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        if (dao.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
        dao.insert(email, hash, role);
        return dao.findByEmail(email).orElseThrow(
            () -> new RuntimeException("User insert failed")
        );
    }
}