package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.GestioneVeicoloAziendaDTO;
import it.unical.fleetgo.backend.Models.Proxy.GestioneVeicoloAziendaProxy;
import it.unical.fleetgo.backend.Persistence.Entity.GestioneVeicoloAzienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class GestioneVeicoloAziendaDAO {

    private final Connection connection;

    public GestioneVeicoloAziendaDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean inserisciNuovoVeicoloGestito(GestioneVeicoloAziendaDTO gestioneVeicoloAzienda) {
        String query = "INSERT INTO gestione_veicolo_azienda(id_veicolo, id_azienda, luogo_ritiro_consegna) VALUES (?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, gestioneVeicoloAzienda.getIdVeicolo());
            ps.setInt(2, gestioneVeicoloAzienda.getIdAzienda());
            ps.setInt(3, gestioneVeicoloAzienda.getIdLuogo());
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

    public Set<GestioneVeicoloAzienda> getVeicoliInGestioneAzienda(Integer idAzienda) {
        String query = "SELECT * FROM gestione_veicolo_azienda WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();
            Set<GestioneVeicoloAzienda> gestioni = new HashSet<>();
            while(rs.next()) {
                GestioneVeicoloAzienda gestione = new GestioneVeicoloAziendaProxy();
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