package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.Proxy.DipendenteProxy;
import it.unical.fleetgo.backend.Models.Proxy.RichiestaAffiliazioneAziendaProxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RichiestaAffiliazioneAziendaDAO {
    Connection con;

    public RichiestaAffiliazioneAziendaDAO(Connection con) {this.con=con;}

    /**
     * Aggiunge una richiesta affiliazione ad un'azienda da parte del dipendente.
     * @param idDipendente
     * @param idAzienda
     * @return
     */
    public boolean aggiungiRichiestaAffiliazioneAzienda(Integer idDipendente,Integer idAzienda){
        String query ="INSERT INTO richiesta_affiliazione_azienda (id_dipendente,id_azienda) VALUES (?,?)";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idDipendente);
            st.setInt(2,idAzienda);
            return st.executeUpdate()>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina una richiesta affiliazione ad un'azienda da parte del dipendente.
     * @param idDipendente
     * @param idAzienda
     * @return
     */
    public boolean rimuoviRichiestaAffiliazioneAzienda(Integer idDipendente,Integer idAzienda){
        String query="DELETE FROM richiesta_affiliazione_azienda WHERE id_dipendente=? AND id_azienda=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idDipendente);
            st.setInt(2,idAzienda);
            return st.executeUpdate()>0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cerca tutti i dipendenti di una data azienda.
     * @param idAzienda
     * @return
     */
    public List<DipendenteProxy> getDipendentiAzienda(Integer idAzienda){
        List<DipendenteProxy> dipendenti=new ArrayList<>();
        String query= "SELECT u.* FROM richiesta_affiliazione_azienda ra JOIN utente u ON ra.id_dipendente = u.id_utente " +
                " WHERE ra.id_azienda = ? AND ra.accettata = ?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idAzienda);
            st.setBoolean(2,true);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                DipendenteProxy dipendente = creaDipendenteProxy();
                dipendente.setIdUtente(rs.getInt("id_utente"));
                dipendente.setNomeUtente(rs.getString("nome_utente"));
                dipendente.setDataNascitaUtente(rs.getDate("data_nascita").toLocalDate());
                dipendente.setCognomeUtente(rs.getString("cognome_utente"));
                dipendenti.add(dipendente);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return dipendenti;
    }

    /**
     * Restituisce le richieste affiliazione da valutare per la propria azienda.
     * Cercherà le richieste con valore "data_accettazione" = null.
     * @param idAzienda
     * @return
     */
    public List<RichiestaAffiliazioneAziendaProxy> getRichiesteAffiliazioneDaValutare(Integer idAzienda){
        List<RichiestaAffiliazioneAziendaProxy> richiesteAffiliazione=new ArrayList<>();
        String query="SELECT * FROM richiesta_affiliazione_azienda WHERE id_azienda=? AND accettata=? AND data_accettazione=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idAzienda);
            st.setBoolean(2,false);
            st.setDate(3,null);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                RichiestaAffiliazioneAziendaProxy richiesta = creaRichiestaAffiliazioneAziendaProxy();
                richiesta.setIdUtente(rs.getInt("id_utente"));
                richiesta.setIdAzienda(rs.getInt("id_azienda"));
                richiesteAffiliazione.add(richiesta);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return richiesteAffiliazione;
    }

    /**
     * Permette di contrassegnare una richiesta di affiliazione come accettata o rifiutata
     * in base al valore del valre booleano accettata.
     * @param idAzienda
     * @param idDipendente
     * @param accettata
     * @return
     */
    public boolean contrassegnaVisionataAffiliazioneAzienda(Integer idAzienda,Integer idDipendente,boolean accettata){
        String query="UPDATE richiesta_affiliazione_azienda SET accettata=?,data_accettazione=CURRENT_DATE WHERE id_azienda=? AND id_dipendente=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setBoolean(1,accettata);
            st.setInt(2,idAzienda);
            st.setInt(3,idDipendente);
            return st.executeUpdate()>0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }



    private DipendenteProxy creaDipendenteProxy(){
        return new DipendenteProxy(new RichiestaAffiliazioneAziendaDAO(con),new CredenzialiDAO(con),new RichiestaNoleggioDAO(con));
    }
    private RichiestaAffiliazioneAziendaProxy creaRichiestaAffiliazioneAziendaProxy(){
        return new RichiestaAffiliazioneAziendaProxy(new UtenteDAO(con));
    }


}
