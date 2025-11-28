package it.unical.fleetgo.backend.Persistence.DAO;

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

            }catch(SQLException e){
            e.printStackTrace();
            con.rollback();
            }finally{con.setAutoCommit(true);}
        return null;
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
            e.printStackTrace();
        }finally{con.setAutoCommit(true);}
    return false;
    }

    /**
     * Permette di accettare o rifiutare una richiesta manutenzione in base al valore
     * della variabile booleana "accettata".
     * @param idManutenzione
     * @param accettata
     * @return
     */
    public boolean contrasegnaRichiestaManutenzione(Integer idManutenzione,boolean accettata){
        String query="UPDATE richiesta_manutenzione SET accettata=? WHERE id_manutenzione=?";
        try(PreparedStatement st =con.prepareStatement(query)){
            st.setBoolean(1,accettata);
            st.setInt(2,idManutenzione);
            return st.executeUpdate()>0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
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
            e.printStackTrace();
        }finally{con.setAutoCommit(true);}
        return false;
    }

    /**
     * Ritorna tutte le richieste di manutenzione ancora da dover accettare.
     * @return
     */
    public List<RichiestaManutenzione> getRichiesteManutenzioneDaAccettare(){
        List<RichiestaManutenzione> richieste = new ArrayList<>();
        String  query="SELECT * FROM richiesta_manutenzione WHERE accettata=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setBoolean(1,false);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                RichiestaManutenzioneProxy richiesta = new RichiestaManutenzioneProxy(new VeicoloDAO(con));
                richiesta.setIdManutenzione(rs.getInt("id_manutenzione"));
                richiesta.setIdAdmin(rs.getInt("id_admin"));
                richiesta.setIdVeicolo(rs.getInt("id_veicolo"));
                richiesta.setDataRichiesta(rs.getDate("data_richiesta").toString());
                richiesta.setTipoManutenzione(rs.getString("tipo_manutenzione"));
                richieste.add(richiesta);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return richieste;
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
            st.setBoolean(1,false);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                RichiestaManutenzioneProxy richiesta = new RichiestaManutenzioneProxy(new VeicoloDAO(con));
                richiesta.setIdManutenzione(rs.getInt("id_manutenzione"));
                richiesta.setIdAdmin(rs.getInt("id_admin"));
                richiesta.setIdVeicolo(rs.getInt("id_veicolo"));
                richiesta.setDataRichiesta(rs.getDate("data_richiesta").toString());
                richiesta.setTipoManutenzione(rs.getString("tipo_manutenzione"));
                richieste.add(richiesta);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return richieste;
    }

    /**
     * Ritorna le richieste effettuate dallo specifico admin aziendale non ancora completate per tenere traccia dello status.
     * @param idAdmin
     * @return
     */
    public List<RichiestaManutenzione> getRichiesteManutenzioneInCorsoAzienda(Integer idAdmin){
        List<RichiestaManutenzione> richieste = new ArrayList<>();
        String  query="SELECT * FROM richiesta_manutenzione WHERE completata=? AND id_admin_azienda=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setBoolean(1,false);
            st.setInt(2,idAdmin);
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
        }catch(SQLException e){
            e.printStackTrace();
        }
        return richieste;
    }
}
