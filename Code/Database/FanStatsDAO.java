package Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FanStatsDAO {

    //  STANDINGS (use mbb_standings_2024_25 table)
    public Object[][] fetchStandingsRows() throws SQLException {
    String sql = """
        SELECT team_name,
               conf_wins,
               conf_losses,
               conf_pct,
               overall_wins,
               overall_losses,
               home_wins,
               home_losses,
               away_wins,
               away_losses,
               neutral_wins,
               neutral_losses,
               streak
        FROM mbb_standings_2024_25
        ORDER BY conf_pct DESC, team_name ASC
    """;

    List<Object[]> rows = new ArrayList<>();

    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        int rank = 1;
        while (rs.next()) {
            String teamName   = rs.getString("team_name");
            int confWins      = rs.getInt("conf_wins");
            int confLosses    = rs.getInt("conf_losses");
            double confPct    = rs.getDouble("conf_pct");  // 0.909 etc.
            int overallWins   = rs.getInt("overall_wins");
            int overallLosses = rs.getInt("overall_losses");
            int homeWins      = rs.getInt("home_wins");
            int homeLosses    = rs.getInt("home_losses");
            int awayWins      = rs.getInt("away_wins");
            int awayLosses    = rs.getInt("away_losses");
            int neutralWins   = rs.getInt("neutral_wins");
            int neutralLosses = rs.getInt("neutral_losses");
            String streak     = rs.getString("streak");

            String confPctStr   = String.format("%.1f", confPct * 100.0);  // ".909" → "90.9"
            String overallStr   = overallWins   + "-" + overallLosses;
            String homeStr      = homeWins      + "-" + homeLosses;
            String awayStr      = awayWins      + "-" + awayLosses;
            String neutralStr   = neutralWins   + "-" + neutralLosses;

            rows.add(new Object[]{
                    rank++,        // #
                    teamName,      // Team
                    confWins,      // Conf W
                    confLosses,    // Conf L
                    confPctStr,    // Conf Win %
                    overallStr,    // Overall
                    homeStr,       // Home
                    awayStr,       // Away
                    neutralStr,    // Neutral
                    streak         // Streak
            });
        }
    }

    return rows.toArray(new Object[0][]);
}

    //  TEAM SUMMARY (for quick stats cards)
    public TeamSummary fetchTeamSummary(String teamName) throws SQLException {
        String sql = """
            SELECT team_name,
                   conf_wins,
                   conf_losses,
                   conf_pct,
                   overall_wins,
                   overall_losses,
                   overall_pct,
                   streak
            FROM mbb_standings_2024_25
            WHERE team_name = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, teamName);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null; // no such team in the table
                }

                TeamSummary ts = new TeamSummary();
                ts.teamName      = rs.getString("team_name");
                ts.confWins      = rs.getInt("conf_wins");
                ts.confLosses    = rs.getInt("conf_losses");
                ts.confPct       = rs.getDouble("conf_pct");       // 0.214
                ts.overallWins   = rs.getInt("overall_wins");
                ts.overallLosses = rs.getInt("overall_losses");
                ts.overallPct    = rs.getDouble("overall_pct");    // 0.214
                ts.streak        = rs.getString("streak");
                return ts;
            }
        }
    }

    
     // Small DTO for summary info about a single team.
    public static class TeamSummary {
        public String teamName;
        public int confWins;
        public int confLosses;
        public double confPct;      // 0.214 = 21.4%
        public int overallWins;
        public int overallLosses;
        public double overallPct;   // 0.214 = 21.4%
        public String streak;
    }

    //  TOP PLAYERS
    public Object[][] fetchTopPlayersRows(String season) throws SQLException {
        String sql = """
            SELECT player_name, team_id, pts, gp, reb, ast
            FROM player_season_stats
            WHERE season = ?
            ORDER BY pts / gp DESC
            LIMIT 10
        """;

        List<Object[]> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, season);
            try (ResultSet rs = ps.executeQuery()) {
                int rank = 1;
                while (rs.next()) {
                    String name   = rs.getString("player_name");
                    int teamId    = rs.getInt("team_id");
                    int pts       = rs.getInt("pts");
                    int gp        = rs.getInt("gp");
                    int reb       = rs.getInt("reb");
                    int ast       = rs.getInt("ast");

                    double ppg = gp > 0 ? (double) pts / gp : 0.0;
                    double rpg = gp > 0 ? (double) reb / gp : 0.0;
                    double apg = gp > 0 ? (double) ast / gp : 0.0;

                    String teamName = "Team " + teamId;

                    // Simple rating placeholder
                    int rating = Math.min(99, (int) Math.round(ppg * 3));

                    list.add(new Object[]{
                            rank++,
                            name,
                            teamName,
                            String.format("%.1f", ppg),
                            String.format("%.1f", rpg),
                            String.format("%.1f", apg),
                            rating
                    });
                }
            }
        }

        return list.toArray(new Object[0][]);
    }


    // Rank of a team within the standings table based on conf_pct
public int fetchTeamRank(String teamName) throws SQLException {
    String sql = """
        SELECT COUNT(*) + 1 AS rank
        FROM mbb_standings_2024_25
        WHERE conf_pct >
              (SELECT conf_pct
               FROM mbb_standings_2024_25
               WHERE team_name = ?)
    """;

    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, teamName);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("rank");
            }
        }
    }
    return -1; 
}

// Average points per game for CSUSM (games table)
public double fetchAvgPointsPerGame() throws SQLException {
    String sql = """
        SELECT AVG(team_score) AS avg_pts
        FROM games
    """;

    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getDouble("avg_pts");
        }
    }
    return 0.0;
}


    //  PERFORMANCE BY MONTH
    public Object[][] fetchPerformanceRows() throws SQLException {
        String sql = """
            SELECT 
              DATE_FORMAT(game_date, '%b') AS month_label,
              AVG(team_score) AS avg_points,
              SUM(CASE WHEN result = 'W' THEN 1 ELSE 0 END) AS wins
            FROM games
            GROUP BY DATE_FORMAT(game_date, '%Y-%m')
            ORDER BY MIN(game_date)
        """;

        List<Object[]> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String month    = rs.getString("month_label");
                int avgPoints   = rs.getInt("avg_points");
                int wins        = rs.getInt("wins");

                list.add(new Object[]{month, avgPoints, wins});
            }
        }

        return list.toArray(new Object[0][]);
    }
}