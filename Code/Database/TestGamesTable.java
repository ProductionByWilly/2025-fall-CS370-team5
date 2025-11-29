package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

public class TestGamesTable {

    public static void main(String[] args) {
        try {
            // Get connection from our Database helper
            Connection conn = Database.getConnection();
            System.out.println("✅ Connected to Railway MySQL (games table test)");

            // Make sure we’re in the correct schema
            try (Statement st = conn.createStatement()) {
                st.execute("USE CSUSportsMetrics"); // change to 'USE railway' if that's your schema
            }

            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"});
            System.out.println("\n📋 Tables in database:");
            while (tables.next()) {
                System.out.println(" - " + tables.getString("TABLE_NAME"));
            }
            // Query the games table
            String sql = "SELECT game_id, game_date, opponent_name, home_away, result, team_score, opp_score, attendance, notes FROM games";

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                System.out.println("\n🎮 Games Table Data:");
                System.out.println("-----------------------------------------------------------");
                System.out.printf("%-5s %-12s %-25s %-8s %-6s %-11s %-11s %-10s %s%n",
                        "ID", "Date", "Opponent", "Venue", "Result", "TeamScore", "OppScore", "Attend", "Notes");
                System.out.println("-----------------------------------------------------------");

                while (rs.next()) {
                    int id = rs.getInt("game_id");
                    String date = rs.getString("game_date");
                    String opponent = rs.getString("opponent_name");
                    String venue = rs.getString("home_away");
                    String result = rs.getString("result");
                    int teamScore = rs.getInt("team_score");
                    int oppScore = rs.getInt("opp_score");
                    int attendance = rs.getInt("attendance");
                    String notes = rs.getString("notes");

                    System.out.printf("%-5d %-12s %-25s %-8s %-6s %-11d %-11d %-10d %s%n",
                            id, date, opponent, venue, result, teamScore, oppScore, attendance, notes == null ? "" : notes);
                }
            }
            // Query the player_season_stats table
            String sqlPlayers = "SELECT * FROM player_season_stats";

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sqlPlayers)) {

                System.out.println("\n🏀 Player Season Stats Table Data:");
                System.out.println("---------------------------------------------------------------");

                // Get metadata to print column headers
                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-20s", rs.getMetaData().getColumnName(i));
                }
                System.out.println();
                System.out.println("---------------------------------------------------------------");

                // Print each row
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        Object val = rs.getObject(i);
                        System.out.printf("%-20s", val == null ? "" : val.toString());
                    }
                    System.out.println();
                }
            }
            conn.close();
            System.out.println("\n✅ Query finished successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}