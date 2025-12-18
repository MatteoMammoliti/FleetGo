package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Models.DTO.GestioneVeicoloAziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloPrenotazioneDTO;
import it.unical.fleetgo.backend.Models.Proxy.GestioneVeicoloAziendaProxy;
import it.unical.fleetgo.backend.Persistence.Entity.GestioneVeicoloAzienda;
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

    public void inserisciNuovoVeicoloGestito(GestioneVeicoloAziendaDTO gestioneVeicoloAzienda) {
        String query = "INSERT INTO gestione_veicolo_azienda(id_veicolo, id_azienda) VALUES (?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, gestioneVeicoloAzienda.getIdVeicolo());
            ps.setInt(2, gestioneVeicoloAzienda.getIdAzienda());
            ps.executeUpdate();
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
                gestione.setDisponbilePerNoleggio(rs.getBoolean("disponbile_per_noleggio"));
                gestioni.add(gestione);
            }
            return gestioni;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void contrassegnaVeicoliLiberiPreEliminazioneAzienda(Integer idAdminAzienda) {
        String query="UPDATE veicolo SET status_contrattuale =? WHERE id_veicolo IN " +
                " (SELECT g.id_veicolo FROM gestione_veicolo_azienda g " +
                "JOIN azienda a ON a.id_azienda=g.id_azienda WHERE a.id_admin_azienda=?)";

        try(PreparedStatement st = connection.prepareStatement(query)){
                st.setString(1, "Disponibile");
                st.setInt(2, idAdminAzienda);
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
        String query="SELECT v.*, CASE " +
                " WHEN v.in_manutenzione=true THEN 'Non_disponibile' " +
                " WHEN EXISTS(" +
                    " SELECT 1 FROM richiesta_noleggio r WHERE r.id_veicolo=v.id_veicolo AND r.stato_richiesta !='Terminata' AND " +
                    " r.richiesta_annullata!=true AND r.accettata=true AND ( (r.data_ritiro + r.ora_inizio) <= ? AND (r.data_consegna + r.ora_fine) >= ? ))" +
                " THEN 'Non_disponibile'" +
                " ELSE 'Disponibile' END as stato_attuale " +
                " FROM gestione_veicolo_azienda ga JOIN veicolo v ON ga.id_veicolo=v.id_veicolo JOIN luogo_azienda l ON l.id_luogo=ga.luogo_ritiro_consegna " +
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
                veicolo.setModello(rs.getString("modello_veicolo"));
                veicolo.setUrlImmagine(rs.getString("immagine_veicolo"));
                veicolo.setTipoDistribuzioneVeicolo(rs.getString("tipo_distribuzione_veicolo"));
                veicolo.setLivelloCarburante(rs.getInt("livello_carburante_veicolo"));
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
}