package repository;

import domain.Tort;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TortDbRepository implements IRepository<Tort> {
    private String url;

    public TortDbRepository(String url) {
        this.url = url;
        createTable();
    }

    private void createTable() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String sql = "CREATE TABLE IF NOT EXISTS Torturi (" +
                "id INTEGER PRIMARY KEY, " +
                "tipulTortului TEXT)";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Tort entity) {
        String sql = "INSERT INTO Torturi(id, tipulTortului) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getTipulTortului());
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM Torturi WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, Tort entity) {
        String sql = "UPDATE Torturi SET tipulTortului = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTipulTortului());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tort findById(int id) {
        String sql = "SELECT * FROM Torturi WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Tort(id, rs.getString("tipulTortului"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Tort> getAll() {
        List<Tort> list = new ArrayList<>();
        String sql = "SELECT * FROM Torturi";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Tort(rs.getInt("id"), rs.getString("tipulTortului")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}