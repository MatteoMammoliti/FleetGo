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
    Connection con;

    public RichiestaNoleggioDAO(Connection con) {this.con=con;}

    /**
     * Aggiunge una nuova richiesta di noleggio.
     * @param richiestaNoleggio
     * @return restituisce l'id della richiesta, null se qualcosa va storto.
     */
    public Integer aggiungiRichiestaNoleggio(RichiestaNoleggioDTO richiestaNoleggio){
        String query="INSERT INTO richiesta_noleggio (id_dipendente,id_azienda,ora_inizio,ora_fine,data_ritiro, " +
                " data_consegna, motivazione) VALUES (?,?,?,?,?,?,?)";
        String dataInizio=richiestaNoleggio.getDataRitiro();
        String dataFine=richiestaNoleggio.getDataConsegna();
        String oraInizio =richiestaNoleggio.getOraInizio();
        String oraFine =richiestaNoleggio.getOraFine();
        try(PreparedStatement st = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)){
            st.setInt(1,richiestaNoleggio.getIdDipendente());
            st.setInt(2,richiestaNoleggio.getIdAziendaRiferimento());
            st.setTime(3, Time.valueOf( LocalTime.parse(oraInizio)));
            st.setTime(4, Time.valueOf(LocalTime.parse(oraFine)));
            st.setDate(5, Date.valueOf(LocalDate.parse(dataInizio)));
            st.setDate(6, Date.valueOf(LocalDate.parse(dataFine)));
            st.setString(7, richiestaNoleggio.getMotivazione());
            st.executeUpdate();
            ResultSet idGenerato = st.getGeneratedKeys();
            if(idGenerato.next()){
                return idGenerato.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean rimuoviRichiestaNoleggio(Integer idRichiesta){
        String query="DELETE FROM richiesta_noleggio WHERE id_richiesta=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idRichiesta);
            return st.executeUpdate()>0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Permette di contrassegnare una richiesta noleggio come accettata o rifiutata
     * in base al valore della variabile booleana accettata.
     * @param idRichiesta
     * @param accettata
     * @return
     */
    public boolean contrassegnaRichiestaNoleggio(Integer idRichiesta,boolean accettata){
        String query="UPDATE richiesta_noleggio SET accettata=? WHERE id_richiesta=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setBoolean(1,accettata);
            st.setInt(2,idRichiesta);
            return st.executeUpdate()>0;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<RichiestaNoleggio> getRichiesteNoleggioAziendaDaAccettare(Integer idAzienda){
        List<RichiestaNoleggio> richiesteNoleggio=new ArrayList<>();
        String query="SELECT * FROM richiesta_noleggio WHERE id_azienda=? AND accettata=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idAzienda);
            st.setBoolean(2,false);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                RichiestaNoleggioProxy richiesta=new RichiestaNoleggioProxy(new UtenteDAO(con));
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
        }catch (SQLException e){
            e.printStackTrace();
        }
        return richiesteNoleggio;
    }
}
