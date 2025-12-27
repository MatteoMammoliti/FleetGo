package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.OffertaDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Offerta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffertaDAO {

    private final Connection connection;

    public OffertaDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserisciOfferta(OffertaDTO offertaDTO) {
        String query = "INSERT INTO offerte_attive (nome_offerta, descrizione_offerta, scadenza, percentuale_sconto, immagine_copertina) VALUES(?, ?, ?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, offertaDTO.getNomeOfferta());
            ps.setString(2, offertaDTO.getDescrizioneOfferta());
            ps.setDate(3, Date.valueOf(offertaDTO.getScadenza()));
            ps.setInt(4, offertaDTO.getPercentualeSconto());
            ps.setString(5, offertaDTO.getImmagineCopertina());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Offerta> getOfferteAttive() {

        this.aggiornaStatoOfferte();

        String query = "SELECT * FROM offerte_attive WHERE offerta_attiva = true";

        try(PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            List<Offerta> offerteDisponibili = new ArrayList<>();

            while(rs.next()) {
                Offerta offerta = new Offerta();

                offerta.setIdOfferta(rs.getInt("id_offerta"));
                offerta.setDescrizioneOfferta(rs.getString("descrizione_offerta"));
                offerta.setNomeOfferta(rs.getString("nome_offerta"));
                offerta.setScadenza(rs.getDate("scadenza").toLocalDate());
                offerta.setPercentualeSconto(rs.getInt("percentuale_sconto"));
                offerta.setImmagineCopertina(rs.getString("immagine_copertina"));

                offerteDisponibili.add(offerta);
            }

            return offerteDisponibili;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminaOfferta(Integer idOfferta) {
        String query = "UPDATE offerte_attive SET offerta_attiva = false WHERE id_offerta = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idOfferta);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Offerta getOffertaById(Integer idOfferta) {
        String query = "SELECT * from offerte_attive WHERE id_offerta = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idOfferta);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Offerta o = new Offerta();
                o.setIdOfferta(rs.getInt("id_offerta"));
                o.setNomeOfferta(rs.getString("nome_offerta"));
                o.setDescrizioneOfferta(rs.getString("descrizione_offerta"));
                o.setScadenza(rs.getDate("scadenza").toLocalDate());
                o.setPercentualeSconto(rs.getInt("percentuale_sconto"));
                o.setImmagineCopertina(rs.getString("immagine_copertina"));
                return o;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void aggiornaStatoOfferte() {
        String query = "UPDATE offerte_attive SET offerta_attiva = false WHERE scadenza < CURRENT_DATE AND offerta_attiva = true";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}