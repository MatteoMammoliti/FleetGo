package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.ModelloDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Modello;
import org.springframework.security.core.parameters.P;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModelloDAO {

    private final Connection connection;

    public ModelloDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean inserisciModello(ModelloDTO modello) {
        String query = "INSERT INTO modelli_veicolo (nome_modello, url_immagine) VALUES (?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, modello.getNomeModello());
            ps.setString(2, modello.getUrlImmagine());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminaModello(Integer idModello) {

        String count = "SELECT COUNT(*) AS numero FROM veicolo WHERE modello_veicolo = ?";

        try(PreparedStatement ps = connection.prepareStatement(count)) {
            ps.setInt(1, idModello);
            ResultSet rs = ps.executeQuery();

            if(rs.next() && rs.getInt("numero") > 0) {
                throw new IllegalStateException("Modello non eliminabile");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "DELETE FROM modelli_veicolo WHERE id_modello=?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idModello);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Modello> getModelli() {
        String query = "SELECT * FROM modelli_veicolo";
        List<Modello> modelli = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Modello m = new Modello();
                m.setIdModello(rs.getInt("id_modello"));
                m.setNomeModello(rs.getString("nome_modello"));
                m.setUrlImmagine(rs.getString("url_immagine"));
                modelli.add(m);
            }
            return modelli;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}