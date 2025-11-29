package Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

public class TestAllTables {

    public static void main(String[] args) {
        try {
            // Get connection from our Database helper
            Connection conn = Database.getConnection();
            System.out.println("✅ Connected to Railway MySQL (all tables test)");

            // Make sure we’re in the correct schema
            try (Statement st = conn.createStatement()) {
                st.execute("USE CSUSportsMetrics");
            }

            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"});
            System.out.println("\n📋 Tables in database:");
            while (tables.next()) {
                System.out.println(" - " + tables.getString("TABLE_NAME"));
            }

            // List of tables to inspect
            String[] tableNames = {
                    "games",
                    "player_season_stats",
                    "team",
                    "team_season_totals",
                    "mbb_standings_2024_25",
                    "users"
            };

            for (String table : tableNames) {
                System.out.println("\n📌 Table: " + table);
                String sql = "SELECT * FROM " + table;
                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery(sql)) {

                    // Get metadata to print column headers
                    int columnCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.printf("%-20s", rs.getMetaData().getColumnName(i));
                    }
                    System.out.println();
                    System.out.println("-".repeat(20 * columnCount));

                    // Print each row
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            Object val = rs.getObject(i);
                            System.out.printf("%-20s", val == null ? "" : val.toString());
                        }
                        System.out.println();
                    }
                } catch (Exception e) {
                    System.out.println("❌ Error querying table " + table + ": " + e.getMessage());
                }
            }

            conn.close();
            System.out.println("\n✅ Query finished successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}