package Database;

import java.sql.*;
import java.util.*;

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

    // ---------------------------------------------------------
    // ACHIEVEMENTS SYSTEM
    // ---------------------------------------------------------

    public static class PlayerAchievement {
        public final String title;
        public final String date;
        public final String tier;

        public PlayerAchievement(String title, String date, String tier) {
            this.title = title;
            this.date  = date;
            this.tier  = tier;
        }
    }

    /**
     * Compute achievements:
     *  - Points (total season points)
     *  - Rebounds (total season rebounds)
     *  - Assists (total season assists)
     *  - Games Played (total games)
     *
     * Tier thresholds based on team percentile:
     *   Bronze  = top 30%  of team or better
     *   Silver  = top 50%  of team or better
     *   Gold    = top 75%  of team or better
     *   Diamond = top 90%  of team or better
     *   Not achieved = below top 30%
     */
    public List<PlayerAchievement> fetchAchievementsForPlayer(int playerId) throws SQLException {

        List<PlayerAchievement> out = new ArrayList<>();

        try (Connection conn = getConnection()) {

            // ---------- 1. Get ALL players' season totals ----------
            String teamSql =
                    "SELECT player_id, " +
                            "SUM(PTS) AS totalPts, " +
                            "SUM(REB) AS totalReb, " +
                            "SUM(AST) AS totalAst, " +
                            "COUNT(*) AS gamesPlayed " +
                            "FROM " + STATS_TABLE + " GROUP BY player_id";

            Map<Integer, double[]> totals = new HashMap<>(); // pid -> [pts, reb, ast, games]

            try (PreparedStatement ps = conn.prepareStatement(teamSql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totals.put(rs.getInt("player_id"), new double[]{
                            rs.getDouble("totalPts"),
                            rs.getDouble("totalReb"),
                            rs.getDouble("totalAst"),
                            rs.getDouble("gamesPlayed")
                    });
                }
            }

            if (!totals.containsKey(playerId)) {
                return out;
            }

            double[] my = totals.get(playerId);
            double myPts   = my[0];
            double myReb   = my[1];
            double myAst   = my[2];
            double myGames = my[3];

            // Build arrays to rank the player vs team
            List<Double> ptsList   = new ArrayList<>();
            List<Double> rebList   = new ArrayList<>();
            List<Double> astList   = new ArrayList<>();
            List<Double> gamesList = new ArrayList<>();

            for (double[] t : totals.values()) {
                ptsList.add(t[0]);
                rebList.add(t[1]);
                astList.add(t[2]);
                gamesList.add(t[3]);
            }

            ptsList.sort(Collections.reverseOrder());
            rebList.sort(Collections.reverseOrder());
            astList.sort(Collections.reverseOrder());
            gamesList.sort(Collections.reverseOrder());

            // Compute tier from percentile
            String ptsTier   = tierFromRank(myPts,   ptsList);
            String rebTier   = tierFromRank(myReb,   rebList);
            String astTier   = tierFromRank(myAst,   astList);
            String gamesTier = tierFromRank(myGames, gamesList);

            // ----------- 2. Get the game date of best performances -----------

            String datePtsSql =
                    "SELECT `Date` FROM " + STATS_TABLE +
                            " WHERE player_id = ? ORDER BY PTS DESC LIMIT 1";

            String dateRebSql =
                    "SELECT `Date` FROM " + STATS_TABLE +
                            " WHERE player_id = ? ORDER BY REB DESC LIMIT 1";

            String dateAstSql =
                    "SELECT `Date` FROM " + STATS_TABLE +
                            " WHERE player_id = ? ORDER BY AST DESC LIMIT 1";

            // For games played, just use first game of the season
            String dateGamesSql =
                    "SELECT `Date` FROM " + STATS_TABLE +
                            " WHERE player_id = ? ORDER BY `Date` ASC LIMIT 1";

            String datePts  = fetchSingleDate(conn, datePtsSql,  playerId);
            String dateReb  = fetchSingleDate(conn, dateRebSql,  playerId);
            String dateAst  = fetchSingleDate(conn, dateAstSql,  playerId);
            String dateGms  = fetchSingleDate(conn, dateGamesSql, playerId);

            // ----------- 3. Push to output -----------

            out.add(new PlayerAchievement("Scoring Achievement",      datePts, ptsTier));
            out.add(new PlayerAchievement("Rebounding Achievement",   dateReb, rebTier));
            out.add(new PlayerAchievement("Assist Achievement",       dateAst, astTier));
            out.add(new PlayerAchievement("Games Played Achievement", dateGms, gamesTier));
        }

        return out;
    }

    private String fetchSingleDate(Connection conn, String sql, int playerId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "--";
    }

    /**
     * Convert a rank into a tier based on where the player's total
     * sits relative to the rest of the team.
     *
     * sortedDesc is a list of totals sorted in descending order.
     */
    private String tierFromRank(double value, List<Double> sortedDesc) {
        if (sortedDesc.isEmpty()) return "Not achieved";

        int idx = sortedDesc.indexOf(value);
        if (idx < 0) return "Not achieved";

        int n = sortedDesc.size();

        // fractionTop = how far into the "top of the team" the player is
        // 1.0 = best in team, 0.0 = last in team
        double fractionTop = 1.0 - ((double) idx / n);

        if (fractionTop >= 0.90) return "Diamond"; // top 90%
        if (fractionTop >= 0.75) return "Gold";    // top 75%
        if (fractionTop >= 0.50) return "Silver";  // top 50%
        if (fractionTop >= 0.30) return "Bronze";  // top 30%
        return "Not achieved";
    }
}
