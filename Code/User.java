public class User {
    public final int id;
    public final String email;
    public final String passwordHash;
    public final Role role;
    public User(int id, String email, String passwordHash, Role role) {
        this.id = id; this.email = email; this.passwordHash = passwordHash; this.role = role;
    }
}
