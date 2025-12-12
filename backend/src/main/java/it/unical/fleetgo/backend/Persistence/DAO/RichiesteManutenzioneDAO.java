package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumericheManutezioni;
import it.unical.fleetgo.backend.Models.DTO.RichiestaManutenzioneDTO;
import it.unical.fleetgo.backend.Models.Proxy.RichiestaManutenzioneProxy;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaManutenzione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RichiesteManutenzioneDAO {
    Connection con;

    public RichiesteManutenzioneDAO(Connection con) {this.con=con;}

    /**
     * Permette di inoltrare una nuova richiesta di manutenzione.Verrà creato il record
     * e aggiornato lo status del veicolo in questione in "Manutenzione".
     * @param richiestaManutenzioneDTO
     * @return
     * @throws SQLException
     */
    public Integer aggiungiRichiestaManutenzione(RichiestaManutenzioneDTO richiestaManutenzioneDTO) throws SQLException {
        String query = "INSERT INTO richiesta_manutenzione (id_admin_azienda,id_veicolo,data_richiesta,tipo_manutenzione) VALUES (?,?,?,?)";
        String aggiornoStatoVeicolo = "UPDATE veicolo SET status_condizione_veicolo=? WHERE id_veicolo=?";

        try{
            con.setAutoCommit(false);

            PreparedStatement st = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setInt(1,richiestaManutenzioneDTO.getIdAdminAzienda());
            st.setInt(2,richiestaManutenzioneDTO.getIdVeicolo());
            st.setDate(3, Date.valueOf(richiestaManutenzioneDTO.getDataRichiesta()));
            st.setString(4,richiestaManutenzioneDTO.getTipoManutenzione());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if(!rs.next()){
                con.rollback();
                return null;
            }

            PreparedStatement st2 = con.prepareStatement(aggiornoStatoVeicolo);
            st2.setString(1,"Manutenzione");
            st2.setInt(2,richiestaManutenzioneDTO.getIdVeicolo());

            if(st2.executeUpdate()==0){
                con.rollback();
                return null;
            }

            con.commit();
            return rs.getInt(1);

            } catch(SQLException e){
                con.rollback();
                throw new RuntimeException(e);

            } finally{
                con.setAutoCommit(true);
        }
    }

    /**
     * Permette di rimuovere una richiesta di manutenzione NON ANCORA ACCETTATA.
     * riporta lo status del veicolo a "Libero".
     * @param idManutenzione
     * @param idVeicolo
     * @return
     * @throws SQLException
     */
    public boolean rimuoviRichiestaManutenzione(Integer idManutenzione,Integer idVeicolo) throws SQLException {
        String eliminoRichiesta = "DELETE FROM richiesta_manutenzione WHERE id_manutenzione=? AND accettata=?";
        String cambioStatusVeicolo = "UPDATE veicolo SET status_condizione_veicolo=? WHERE id_veicolo=?";

        try{
            con.setAutoCommit(false);
            PreparedStatement st = con.prepareStatement(eliminoRichiesta);
            st.setInt(1,idManutenzione);
            st.setBoolean(2,false);
            int riga=st.executeUpdate();

            if(riga==0){
                con.rollback();
                return false;
            }

            PreparedStatement st2 = con.prepareStatement(cambioStatusVeicolo);
            st2.setString(1,"Libero");
            st2.setInt(2,idVeicolo);

            if(st2.executeUpdate()==0){
                con.rollback();
                return false;
            }

            con.commit();
            return true;

        }catch(SQLException e){
            con.rollback();
            throw new RuntimeException(e);
        } finally{
            con.setAutoCommit(true);
        }
    }

    /**
     * Permette di accettare o rifiutare una richiesta manutenzione in base al valore
     * della variabile booleana "accettata".
     * @param idManutenzione
     * @param accettata
     * @return
     */
    public boolean contrassegnaRichiestaManutenzione(Integer idManutenzione, boolean accettata) throws SQLException {
        String recuperoIdVeicolo="SELECT id_veicolo FROM richiesta_manutenzione WHERE id_manutenzione=?";
        String aggiornoRichiesta="UPDATE richiesta_manutenzione SET accettata=? WHERE id_manutenzione=?";
        String aggiornoVeicolo="UPDATE veicolo SET in_manutenzione=? WHERE id_veicolo=?";
        try {
            con.setAutoCommit(false);
            Integer idVeicolo = null;
            try (PreparedStatement st = con.prepareStatement(recuperoIdVeicolo)) {
                st.setInt(1,idManutenzione);
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    idVeicolo = rs.getInt(1);
                }
                if(idVeicolo==null){
                    con.rollback();
                    return false;
                }
                try(PreparedStatement st2 = con.prepareStatement(aggiornoRichiesta)){
                    st2.setBoolean(1,accettata);
                    st2.setInt(2,idManutenzione);
                    if(st2.executeUpdate()==0){
                        con.rollback();
                        return false;
                    }
                }
                if(accettata){
                    try(PreparedStatement st3 = con.prepareStatement(aggiornoVeicolo)){
                        st3.setBoolean(1,accettata);
                        st3.setInt(2,idVeicolo);
                        if(st3.executeUpdate()==0){
                            con.rollback();
                            return false;
                        }
                    }
                }
                con.commit();
                return true;
            }
        catch(SQLException e){
            con.rollback();
            throw e;
            }
        }
        finally{
            con.setAutoCommit(true);
        }
    }

    public boolean contrassegnaRichiestaManutenzioneComeCompletata(Integer idManutenzione,Integer idVeicolo) throws SQLException {
        String modificaRichiesta="UPDATE richiesta_manutenzione SET completata=? WHERE id_manutenzione=?";
        String cambioStatusVeicolo="UPDATE veicolo SET status_condizione_veicolo=? WHERE id_veicolo=?";
        try {
            con.setAutoCommit(false);
            PreparedStatement st = con.prepareStatement(modificaRichiesta);
            st.setBoolean(1,true);
            st.setInt(2,idManutenzione);
            if(st.executeUpdate()==0){
                con.rollback();
                return false;
            }
            PreparedStatement st2 = con.prepareStatement(cambioStatusVeicolo);
            st2.setInt(1,idVeicolo);
            if(st2.executeUpdate()==0){
                con.rollback();
                return false;
            }
            con.commit();
            return true;
        }catch(SQLException e){
            con.rollback();
            throw new RuntimeException(e);
        }finally{
            con.setAutoCommit(true);
        }
    }

    public RichiestaManutenzione getRichiestaManutenzione(Integer idManutenzione) throws SQLException {
        String query="SELECT * FROM richiesta_manutenzione WHERE id_manutenzione=?";
        try(PreparedStatement st =con.prepareStatement(query)){
            st.setInt(1,idManutenzione);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                RichiestaManutenzioneProxy richiesta = new RichiestaManutenzioneProxy(new VeicoloDAO(con));
                estraiRichiestaSingolaManutenzione(richiesta,st);
                return richiesta;

            }
        }
        return null;
    }

    /**
     * Ritorna tutte le richieste di manutenzione ancora da dover accettare.
     * @return
     */
    public List<RichiestaManutenzione> getRichiesteManutenzioneDaAccettare(){
        List<RichiestaManutenzione> richieste = new ArrayList<>();
        String  query="SELECT * FROM richiesta_manutenzione WHERE accettata IS NULL";
        try(PreparedStatement st = con.prepareStatement(query)){
            estraiRichiesteManutenzione(richieste, st);
            return richieste;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Ritorna tutte le richieste TUTTE le manutenzione accettate ma non ancora completate.
     * @return
     */
    public List<RichiestaManutenzione> getRichiesteManutenzioneInCorso(){
        List<RichiestaManutenzione> richieste = new ArrayList<>();
        String  query="SELECT * FROM richiesta_manutenzione WHERE accettata=? AND completata=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setBoolean(1,true);
            st.setBoolean(2,false);
            estraiRichiesteManutenzione(richieste, st);
            return richieste;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<RichiestaManutenzione> getRichiesteManutenzioneStorico(){
        List<RichiestaManutenzione> richieste = new ArrayList<>();
        String  query="SELECT * FROM richiesta_manutenzione WHERE accettata=? AND completata=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setBoolean(1,true);
            st.setBoolean(2,true);
            estraiRichiesteManutenzione(richieste, st);
            return richieste;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Ritorna le richieste effettuate dallo specifico admin aziendale non ancora completate per tenere traccia dello status.
     * @param idAdmin
     * @return
     */
    public List<RichiestaManutenzione> getRichiesteManutenzioneInCorsoAzienda(Integer idAdmin){
        List<RichiestaManutenzione> richieste = new ArrayList<>();
        String  query="SELECT * FROM richiesta_manutenzione WHERE completata=?  AND accettata=? AND id_admin_azienda=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setBoolean(1,false);
            st.setBoolean(2,true);
            st.setInt(3,idAdmin);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                RichiestaManutenzioneProxy richiesta = new RichiestaManutenzioneProxy(new VeicoloDAO(con));
                richiesta.setIdManutenzione(rs.getInt("id_manutenzione"));
                richiesta.setIdAdmin(rs.getInt("id_admin"));
                richiesta.setIdVeicolo(rs.getInt("id_veicolo"));
                richiesta.setDataRichiesta(rs.getDate("data_richiesta").toString());
                richiesta.setTipoManutenzione(rs.getString("tipo_manutenzione"));
                richiesta.setRichiestaAccettata(rs.getBoolean("accettata"));
                richiesta.setRichiestaCompletata(rs.getBoolean("completata"));
                richieste.add(richiesta);
            }

            return richieste;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public ContenitoreStatisticheNumericheManutezioni getStatisticheManutenzioni() throws SQLException {
        String query="SELECT " +
                " (SELECT COUNT(*) FROM richiesta_manutenzione WHERE accettata=true AND completata=false) as attualmente_in_corso," +
                " (SELECT COUNT(*) FROM richiesta_manutenzione WHERE accettata=true AND completata=true) as interventi_conclusi";
        try(PreparedStatement st = con.prepareStatement(query)){
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                ContenitoreStatisticheNumericheManutezioni contenitore = new ContenitoreStatisticheNumericheManutezioni();
                contenitore.setInterventiConclusi(rs.getInt("interventi_conclusi"));
                contenitore.setAttualmenteInOfficina(rs.getInt("attualmente_in_corso"));
                return contenitore;
            }
            return null;
        }
    }

    private void estraiRichiesteManutenzione(List<RichiestaManutenzione> richieste, PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        while(rs.next()){
            RichiestaManutenzioneProxy richiesta = new RichiestaManutenzioneProxy(new VeicoloDAO(con));
            richiesta.setIdManutenzione(rs.getInt("id_manutenzione"));
            richiesta.setIdAdmin(rs.getInt("id_admin_azienda"));
            richiesta.setIdVeicolo(rs.getInt("id_veicolo"));
            richiesta.setDataRichiesta(rs.getDate("data_richiesta").toString());
            richiesta.setTipoManutenzione(rs.getString("tipo_manutenzione"));
            richiesta.setRichiestaAccettata(rs.getBoolean("accettata"));
            richiesta.setRichiestaCompletata(rs.getBoolean("completata"));
            richieste.add(richiesta);
        }
    }
    private void estraiRichiestaSingolaManutenzione(RichiestaManutenzione richiesta, PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        if (rs.next()){
            richiesta.setIdManutenzione(rs.getInt("id_manutenzione"));
            richiesta.setIdAdmin(rs.getInt("id_admin_azienda"));
            richiesta.setIdVeicolo(rs.getInt("id_veicolo"));
            richiesta.setDataRichiesta(rs.getDate("data_richiesta").toString());
            richiesta.setTipoManutenzione(rs.getString("tipo_manutenzione"));
            richiesta.setRichiestaAccettata(rs.getBoolean("accettata"));
            richiesta.setRichiestaCompletata(rs.getBoolean("completata"));
        }
    }
}