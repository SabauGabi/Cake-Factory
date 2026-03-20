package repository;

import domain.Comanda;
import domain.Tort;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComandaDbRepository implements IRepository<Comanda> {
    private String url;

    public ComandaDbRepository(String url) {
        this.url = url;
        createTable();
    }

    private void createTable() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS Comenzi (" +
                    "id INTEGER PRIMARY KEY, " +
                    "data INTEGER)");


            stmt.execute("CREATE TABLE IF NOT EXISTS Comenzi_Torturi (" +
                    "comanda_id INTEGER, " +
                    "tort_id INTEGER, " +
                    "FOREIGN KEY(comanda_id) REFERENCES Comenzi(id), " +
                    "FOREIGN KEY(tort_id) REFERENCES Torturi(id))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Comanda entity) {
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);


            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Comenzi(id, data) VALUES(?, ?)")) {
                ps.setInt(1, entity.getId());
                ps.setLong(2, entity.getData().getTime());
                ps.executeUpdate();
            }


            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Comenzi_Torturi(comanda_id, tort_id) VALUES(?, ?)")) {
                for (Tort t : entity.getListaTorturi()) {
                    ps.setInt(1, entity.getId());
                    ps.setInt(2, t.getId());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);


            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Comenzi_Torturi WHERE comanda_id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Comenzi WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, Comanda entity) {
        remove(id);
        add(entity);
    }

    @Override
    public Comanda findById(int id) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Comenzi WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                long timestamp = rs.getLong("data");
                java.util.Date data = new java.util.Date(timestamp);
                List<Tort> torturi = getTorturiForComanda(id, conn);
                return new Comanda(id, torturi, data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Comanda> getAll() {
        List<Comanda> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Comenzi")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                long timestamp = rs.getLong("data");
                java.util.Date data = new java.util.Date(timestamp);
                List<Tort> torturi = getTorturiForComanda(id, conn);

                list.add(new Comanda(id, torturi, data));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Tort> getTorturiForComanda(int comandaId, Connection conn) throws SQLException {
        List<Tort> list = new ArrayList<>();
        String sql = "SELECT t.id, t.tipulTortului FROM Torturi t " +
                "JOIN Comenzi_Torturi ct ON t.id = ct.tort_id " +
                "WHERE ct.comanda_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comandaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Tort(rs.getInt("id"), rs.getString("tipulTortului")));
            }
        }
        return list;
    }
}