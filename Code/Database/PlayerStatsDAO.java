package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerStatsDAO {

    // Use THIS table for all player stats
    private static final String STATS_TABLE = "Player_Game_Stats";

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    /**
     * Map a logged-in user to their player_id.
     * users.id -> player_accounts.user_id -> player_accounts.player_id
     */
    public Integer findPlayerIdForUser(int userId) throws SQLException {
        final String sql =
                "SELECT player_id " +
                        "FROM player_accounts " +
                        "WHERE user_id = ? " +
                        "LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("player_id");
                }
            }
        }
        return null;
    }

    // ---------- Season summary (PPG / RPG / APG) ----------

    public static class PlayerSummary {
        public final int playerId;
        public final String playerName;
        public final String jersey;
        public final double ppg;
        public final double rpg;
        public final double apg;

        public PlayerSummary(int playerId, String playerName, String jersey,
                             double ppg, double rpg, double apg) {
            this.playerId   = playerId;
            this.playerName = playerName;
            this.jersey     = jersey;
            this.ppg        = ppg;
            this.rpg        = rpg;
            this.apg        = apg;
        }
    }

    /**
     * Season-level averages for a player from Player_Game_Stats.
     * (No season filter so it just averages all rows we have for that player.)
     */
    public PlayerSummary fetchPlayerSummary(int playerId) throws SQLException {
        final String sql =
                "SELECT player_id, player_name, jersey, " +
                        "       AVG(PTS) AS ppg, " +
                        "       AVG(REB) AS rpg, " +
                        "       AVG(AST) AS apg " +
                        "FROM " + STATS_TABLE + " " +
                        "WHERE player_id = ? " +
                        "GROUP BY player_id, player_name, jersey";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name   = rs.getString("player_name");
                    String jersey = rs.getString("jersey");
                    double ppg    = rs.getDouble("ppg");
                    double rpg    = rs.getDouble("rpg");
                    double apg    = rs.getDouble("apg");

                    return new PlayerSummary(playerId, name, jersey, ppg, rpg, apg);
                }
            }
        }
        return null;
    }

    // ---------- Per-game rows for the Performance table ----------

    /**
     * Returns rows for the performance JTable in PlayerPanel:
     *   [ "GameLabel", "Opponent", "Result", PTS, REB, AST ]
     *
     * Result is pulled from the games table. If games.result is null,
     * we build something like "W 75-60" or "L 60-75" from team_score/opp_score.
     */
    public Object[][] fetchGameRowsForPlayer(int playerId) throws SQLException {

        final String sql =
                "SELECT " +
                        "  pgs.game_id, " +
                        "  pgs.`Date`       AS game_date, " +
                        "  pgs.Opponent    AS opponent, " +
                        "  COALESCE( " +
                        "      g.result, " +
                        "      CASE " +
                        "          WHEN g.team_score IS NULL OR g.opp_score IS NULL THEN '' " +
                        "          WHEN g.team_score > g.opp_score " +
                        "              THEN CONCAT('W ', g.team_score, '-', g.opp_score) " +
                        "          WHEN g.team_score < g.opp_score " +
                        "              THEN CONCAT('L ', g.team_score, '-', g.opp_score) " +
                        "          ELSE CONCAT('T ', g.team_score, '-', g.opp_score) " +
                        "      END " +
                        "  ) AS result_text, " +
                        "  pgs.PTS, " +
                        "  pgs.REB, " +
                        "  pgs.AST " +
                        "FROM " + STATS_TABLE + " pgs " +
                        "LEFT JOIN games g ON g.game_id = pgs.game_id " +
                        "WHERE pgs.player_id = ? " +
                        "ORDER BY pgs.game_id DESC";

        List<Object[]> rows = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int gameId       = rs.getInt("game_id");
                    String gameDate  = rs.getString("game_date");
                    String opponent  = rs.getString("opponent");
                    String result    = rs.getString("result_text");
                    int pts          = rs.getInt("PTS");
                    int reb          = rs.getInt("REB");
                    int ast          = rs.getInt("AST");

                    String gameLabel = "#" + gameId + "  " + gameDate;

                    rows.add(new Object[] {
                            gameLabel,
                            opponent,
                            result != null ? result : "",
                            pts,
                            reb,
                            ast
                    });
                }
            }
        }

        return rows.toArray(new Object[0][]);
    }
}