package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.GestioneVeicoloAziendaDTO;
import it.unical.fleetgo.backend.Models.Proxy.GestioneVeicoloAziendaProxy;
import it.unical.fleetgo.backend.Persistence.Entity.GestioneVeicoloAzienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestioneVeicoloAziendaDAO {

    private final Connection connection;

    public GestioneVeicoloAziendaDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean inserisciNuovoVeicoloGestito(GestioneVeicoloAziendaDTO gestioneVeicoloAzienda) {
        String query = "INSERT INTO gestione_veicolo_azienda(id_veicolo, id_azienda) VALUES (?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, gestioneVeicoloAzienda.getIdVeicolo());
            ps.setInt(2, gestioneVeicoloAzienda.getIdAzienda());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminaVeicoloGestito(GestioneVeicoloAziendaDTO gestioneVeicoloAzienda) {
        String query = "DELETE FROM gestione_veicolo_azienda WHERE id_veicolo = ? AND id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, gestioneVeicoloAzienda.getIdVeicolo());
            ps.setInt(2, gestioneVeicoloAzienda.getIdAzienda());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<GestioneVeicoloAzienda> getVeicoliInGestioneAzienda(Integer idAzienda) {
        String query = "SELECT * FROM gestione_veicolo_azienda WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();
            List<GestioneVeicoloAzienda> gestioni = new ArrayList<>();
            while(rs.next()) {
                GestioneVeicoloAzienda gestione = new GestioneVeicoloAziendaProxy(new VeicoloDAO(connection),new LuogoAziendaDAO(connection));
                gestione.setIdVeicolo(rs.getInt("id_veicolo"));
                gestione.setIdAzienda(rs.getInt("id_azienda"));
                gestione.setIdLuogo(rs.getInt("id_luogo"));
                gestioni.add(gestione);
            }
            return gestioni;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}