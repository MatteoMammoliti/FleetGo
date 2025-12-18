package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.Proxy.RichiestaNoleggioProxy;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RichiestaNoleggioDAO {

    private final Connection connection;

    public RichiestaNoleggioDAO(Connection con) {
        this.connection = con;
    }

    /**
     * Aggiunge una nuova richiesta di noleggio.
     * @param richiestaNoleggio
     * @return restituisce l'id della richiesta, null se qualcosa va storto.
     */
    public Integer aggiungiRichiestaNoleggio(RichiestaNoleggioDTO richiestaNoleggio){
        String query="INSERT INTO richiesta_noleggio (id_dipendente,id_azienda,ora_inizio,ora_fine,data_ritiro, " +
                " data_consegna, motivazione, id_veicolo, costo_noleggio) VALUES (?,?,?,?,?,?,?, ?)";

        String dataInizio=richiestaNoleggio.getDataRitiro();
        String dataFine=richiestaNoleggio.getDataConsegna();
        String oraInizio =richiestaNoleggio.getOraInizio();
        String oraFine =richiestaNoleggio.getOraFine();

        try(PreparedStatement st = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)){
            st.setInt(1,richiestaNoleggio.getIdDipendente());
            st.setInt(2,richiestaNoleggio.getIdAziendaRiferimento());
            st.setTime(3, Time.valueOf( LocalTime.parse(oraInizio)));
            st.setTime(4, Time.valueOf(LocalTime.parse(oraFine)));
            st.setDate(5, Date.valueOf(LocalDate.parse(dataInizio)));
            st.setDate(6, Date.valueOf(LocalDate.parse(dataFine)));
            st.setString(7, richiestaNoleggio.getMotivazione());
            st.setInt(8, richiestaNoleggio.getIdVeicolo());
            st.executeUpdate();
            ResultSet idGenerato = st.getGeneratedKeys();

            if(idGenerato.next()) return idGenerato.getInt(1);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean rimuoviRichiestaNoleggio(Integer idRichiesta){
        String query="DELETE FROM richiesta_noleggio WHERE id_richiesta=?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idRichiesta);
            return st.executeUpdate()>0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Permette di contrassegnare una richiesta noleggio come accettata o rifiutata
     * in base al valore della variabile booleana accettata.
     * @param idRichiesta
     * @param accettata
     * @return
     */
    public boolean accettaRichiestaNoleggio(Integer idRichiesta){
        String query="UPDATE richiesta_noleggio SET accettata=? AND stato_richiesta = ? WHERE id_richiesta=?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setBoolean(1, true);
            st.setString(2, "Da ritirare");
            st.setInt(2,idRichiesta);
            return st.executeUpdate()>0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean rifiutaRichiestaNoleggio(Integer idRichiesta){
        String query="UPDATE richiesta_noleggio SET accettata=? WHERE id_richiesta=?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setBoolean(1, false);
            st.setInt(2,idRichiesta);
            return st.executeUpdate()>0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<RichiestaNoleggio> getRichiesteNoleggioAziendaDaAccettare(Integer idAzienda){
        String query="SELECT * FROM richiesta_noleggio WHERE id_azienda=? AND accettata=?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idAzienda);
            st.setBoolean(2,false);
            ResultSet rs = st.executeQuery();

            List<RichiestaNoleggio> richiesteNoleggio=new ArrayList<>();

            while(rs.next()){
                RichiestaNoleggioProxy richiesta=new RichiestaNoleggioProxy(new UtenteDAO(connection));
                richiesta.setIdRichiestaNoleggio(rs.getInt("id_richiesta"));
                richiesta.setIdUtente(rs.getInt("id_dipendente"));
                richiesta.setOraInizio(rs.getTime("ora_inizio").toLocalTime());
                richiesta.setOraFine(rs.getTime("ora_fine").toLocalTime());
                richiesta.setDataRitiro(rs.getDate("data_ritiro").toLocalDate());
                richiesta.setDataConsegna(rs.getDate("data_consegna").toLocalDate());
                richiesta.setMotivazione(rs.getString("motivazione"));
                richiesta.setRichiestaAccettata(rs.getBoolean("accettata"));
                richiesta.setIdVeicolo(rs.getInt("id_veicolo"));
                richiesteNoleggio.add(richiesta);
            }

            return richiesteNoleggio;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Integer getNumRichiesteNoleggio(Integer idAzienda) {
        String query = "SELECT COUNT(*) as somma FROM richiesta_noleggio WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getInt("somma");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<RichiestaNoleggio> getRichiesteNoleggioAccettateByIdDipendente(Integer idDipendente) {
        String query = "SELECT * FROM richiesta_noleggio WHERE id_dipendente = ? AND accettata = true";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idDipendente);

            ResultSet rs = ps.executeQuery();
            List<RichiestaNoleggio> richieste = new ArrayList<>();

            while (rs.next()) {
                RichiestaNoleggio r = new RichiestaNoleggio();
                r.setIdRichiestaNoleggio(rs.getInt("id_richiesta"));
                r.setIdUtente(rs.getInt("id_dipendente"));
                r.setIdAzienda(rs.getInt("id_azienda"));
                r.setOraInizio(rs.getTime("ora_inizio").toLocalTime());
                r.setOraFine(rs.getTime("ora_fine").toLocalTime());
                r.setDataRitiro(rs.getDate("data_ritiro").toLocalDate());
                r.setDataConsegna(rs.getDate("data_consegna").toLocalDate());
                r.setMotivazione(rs.getString("motivazione"));
                r.setIdVeicolo(rs.getInt("id_veicolo"));
                r.setCostoNoleggio(rs.getInt("costo_noleggio"));
                r.setRichiestaAccettata(rs.getBoolean("accettata"));
                r.setStatoRichiesta(rs.getString("stato_richiesta"));
                richieste.add(r);
            }

            return richieste;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}