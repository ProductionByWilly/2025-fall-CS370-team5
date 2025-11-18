// LeaderboardService class for accessing database
import java.sql.SQLException;

public class LeaderboardService {
    public List<PlayerRecord> getLeaderboard() throws SQLException {
        List<PlayerRecord> list = new ArrayList<>();
        Connection conn = Database.getConnection(); // Connect to database

        String sql = "SELECT player_name, score FROM leaderboard ORDER BY score DESC";
        PreparedStatement st = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            list.add(new PlayerRecord(
                    rs.getString("player_name"),
                    rs.getInt("score")
            ));
        }
        return list;
    }
}
