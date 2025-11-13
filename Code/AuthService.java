import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;

public class AuthService {
    private final UserDAO dao = new UserDAO();

    public User signIn(String email, String rawPassword) {
        Optional<User> opt = dao.findByEmail(email);
        if (opt.isEmpty()) throw new IllegalArgumentException("Invalid email or password");
        User u = opt.get();
        if (!BCrypt.checkpw(rawPassword, u.passwordHash))
            throw new IllegalArgumentException("Invalid email or password");
        return u;
    }
}