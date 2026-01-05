package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.VeicoloPrenotazione;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestioneVeicoloAziendaDAO {

    private final Connection connection;

    public GestioneVeicoloAziendaDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserisciNuovoVeicoloGestito(Integer idVeicolo, Integer idAzienda) throws SQLException {
        try {

            connection.setAutoCommit(false);

            String query = "INSERT INTO gestione_veicolo_azienda(id_veicolo, id_azienda) VALUES (?, ?)";

            try(PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, idVeicolo);
                ps.setInt(2, idAzienda);
                ps.executeUpdate();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }

            String aggiornamentoStatusContrattuale = "UPDATE veicolo SET status_contrattuale = 'Noleggiato' WHERE id_veicolo = ?";

            try(PreparedStatement ps = connection.prepareStatement(aggiornamentoStatusContrattuale)) {
                ps.setInt(1, idVeicolo);
                ps.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }

            connection.commit();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean eliminaVeicoloGestito(Integer idVeicolo) throws SQLException {

        String query = "DELETE FROM gestione_veicolo_azienda WHERE id_veicolo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idVeicolo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminaVeicoliInGestioneAzienda(Integer idAzienda) {
        String query = "DELETE FROM gestione_veicolo_azienda WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void contrassegnaVeicoliLiberiPreDisabilitazioneAzienda(Integer idAzienda) {
        String query="UPDATE veicolo SET status_contrattuale =? WHERE id_veicolo IN " +
                " (SELECT g.id_veicolo FROM gestione_veicolo_azienda g WHERE g.id_azienda = ?)";

        try(PreparedStatement st = connection.prepareStatement(query)){
                st.setString(1, "Disponibile");
                st.setInt(2, idAzienda);
                st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<VeicoloPrenotazione> veicoliPrenotazione(Integer idAzienda, String dataRitiro, String dataConsegna,
                                                         String oraInizio, String oraFine,String nomeLuogo) throws SQLException{
        RichiestaNoleggioDAO dao = new RichiestaNoleggioDAO(connection);
        dao.aggiornaStatiNoleggi();
        LocalDateTime inizioRichiesta = LocalDateTime.parse(dataRitiro + "T" + oraInizio);
        LocalDateTime fineRichiesta = LocalDateTime.parse(dataConsegna + "T" + oraFine);
        List<VeicoloPrenotazione> veicoli = new ArrayList<>();
        String query="SELECT v.*, m.nome_modello, m.url_immagine, CASE " +
                " WHEN v.in_manutenzione=true THEN 'Non_disponibile' " +
                " WHEN EXISTS(" +
                    " SELECT 1 FROM richiesta_noleggio r WHERE r.id_veicolo=v.id_veicolo AND r.stato_richiesta !='Terminata' AND " +
                    " r.richiesta_annullata!=true AND r.accettata=true AND ( (r.data_ritiro + r.ora_inizio) <= ? AND (r.data_consegna + r.ora_fine) >= ? ))" +
                " THEN 'Non_disponibile'" +
                " ELSE 'Disponibile' END as stato_attuale " +
                " FROM gestione_veicolo_azienda ga JOIN veicolo v ON ga.id_veicolo=v.id_veicolo JOIN luogo_azienda l ON l.id_luogo=ga.luogo_ritiro_consegna JOIN modelli_veicolo m ON v.modello_veicolo = m.id_modello " +
                " WHERE ga.id_azienda=? AND l.nome_luogo=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setTimestamp(1, Timestamp.valueOf(fineRichiesta));
            st.setTimestamp(2, Timestamp.valueOf(inizioRichiesta));
            st.setInt(3, idAzienda);
            st.setString(4, nomeLuogo);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                VeicoloPrenotazione veicolo = new VeicoloPrenotazione();
                LuogoAzienda luogo = new LuogoAzienda();
                veicolo.setIdVeicolo(rs.getInt("id_veicolo"));
                veicolo.setTargaVeicolo(rs.getString("targa"));
                veicolo.setNomeModello(rs.getString("nome_modello"));
                veicolo.setIdModello(rs.getInt("modello_veicolo"));
                veicolo.setUrlImmagine(rs.getString("url_immagine"));
                veicolo.setTipoDistribuzioneVeicolo(rs.getString("tipo_distribuzione_veicolo"));
                veicolo.setStatusContrattualeVeicolo(rs.getString("status_contrattuale"));
                veicolo.setInManutenzione(rs.getBoolean("in_manutenzione"));
                veicolo.setStatoAttuale(rs.getString("stato_attuale"));
                luogo.setNomeLuogo(nomeLuogo);
                veicolo.setLuogo(luogo);
                veicoli.add(veicolo);
            }
            return veicoli;
        }
    }


    public void impostaLuogoVeicolo(VeicoloDTO veicoloDTO) {
        String query = "UPDATE gestione_veicolo_azienda SET luogo_ritiro_consegna = ?, disponibile_per_noleggio = true WHERE id_veicolo = ? AND id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, veicoloDTO.getLuogoRitiroDeposito().getIdLuogo());
            ps.setInt(2, veicoloDTO.getIdVeicolo());
            ps.setInt(3, veicoloDTO.getIdAziendaAffiliata());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getNumeroVeicoliAssegnatiAzienda(Integer idAzienda) {
        String query = "SELECT COUNT(*) FROM veicolo v LEFT JOIN  gestione_veicolo_azienda g ON v.id_veicolo=g.id_veicolo LEFT JOIN azienda a " +
                " ON a.id_azienda = g.id_azienda JOIN modelli_veicolo m ON v.modello_veicolo = m.id_modello WHERE a.id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getNumeroAutoSenzaLuogo(Integer idAzienda) {
        String query = "SELECT COUNT(*) FROM gestione_veicolo_azienda WHERE id_azienda = ? AND luogo_ritiro_consegna IS NULL";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}