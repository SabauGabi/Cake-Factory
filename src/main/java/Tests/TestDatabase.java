package Tests;

import java.sql.*;
import java.io.File;

public class TestDatabase {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:data.db"; // Locatia bazei de date

        System.out.println("1. Incerc conectarea la: " + url);

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("2. Conexiune reusita!");

                // Creare tabel
                String sqlCreate = "CREATE TABLE IF NOT EXISTS TestTable (id INTEGER PRIMARY KEY, nume TEXT)";
                Statement stmt = conn.createStatement();
                stmt.execute(sqlCreate);
                System.out.println("3. Tabel creat (sau exista deja).");

                // Inserare date
                String sqlInsert = "INSERT INTO TestTable(id, nume) VALUES(1, 'TestTort')";
                try {
                    Statement stmt2 = conn.createStatement();
                    stmt2.execute(sqlInsert);
                    System.out.println("4. Date inserate cu succes.");
                } catch (SQLException e) {
                    System.out.println("   (Nota: Probabil ID-ul 1 exista deja, e ok).");
                }

                // Citire date
                Statement stmt3 = conn.createStatement();
                ResultSet rs = stmt3.executeQuery("SELECT * FROM TestTable");
                while(rs.next()) {
                    System.out.println("   -> Gasit in DB: " + rs.getInt("id") + " - " + rs.getString("nume"));
                }

            }
        } catch (SQLException e) {
            System.err.println("EROARE CRITICA SQL:");
            e.printStackTrace();
        }

        // Verificam daca fisierul exista fizic
        File f = new File("data.db");
        System.out.println("5. Fisierul data.db exista? " + f.exists());
        System.out.println("6. Cale absoluta: " + f.getAbsolutePath());
        System.out.println("7. Dimensiune: " + f.length() + " bytes");
    }
}