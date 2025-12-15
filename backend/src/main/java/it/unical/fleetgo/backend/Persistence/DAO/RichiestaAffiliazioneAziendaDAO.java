package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.Proxy.DipendenteProxy;
import it.unical.fleetgo.backend.Models.Proxy.RichiestaAffiliazioneAziendaProxy;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaAffiliazioneAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RichiestaAffiliazioneAziendaDAO {
    private final Connection connection;

    public RichiestaAffiliazioneAziendaDAO(Connection con) {this.connection =con;}

    /**
     * Aggiunge una richiesta affiliazione ad un'azienda da parte del dipendente.
     * @param idDipendente
     * @param idAzienda
     * @return
     */
    public boolean aggiungiRichiestaAffiliazioneAzienda(Integer idDipendente,Integer idAzienda){
        String query ="INSERT INTO richiesta_affiliazione_azienda (id_dipendente,id_azienda) VALUES (?,?)";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idDipendente);
            st.setInt(2,idAzienda);
            return st.executeUpdate()>0;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Elimina una richiesta affiliazione ad un'azienda da parte del dipendente.
     *
     * @param idDipendente
     * @param idAzienda
     */
    public void rimuoviRichiestaAffiliazioneAzienda(Integer idDipendente, Integer idAzienda){
        String query="DELETE FROM richiesta_affiliazione_azienda WHERE id_dipendente=? AND id_azienda=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idDipendente);
            st.setInt(2,idAzienda);
            st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Cerca tutti i dipendenti di una data azienda.
     * @param idAzienda
     * @return
     */
    public List<Dipendente> getDipendentiAzienda(Integer idAzienda) throws RuntimeException {
        List<Dipendente> dipendenti=new ArrayList<>();

        String query= "SELECT u.* FROM richiesta_affiliazione_azienda ra JOIN utente u ON ra.id_dipendente = u.id_utente " +
                " WHERE ra.id_azienda = ? AND ra.accettata = ?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idAzienda);
            st.setBoolean(2,true);
            ResultSet rs = st.executeQuery();

            while(rs.next()){

                DipendenteProxy dipendente = creaDipendenteProxy();
                dipendente.setIdUtente(rs.getInt("id_utente"));
                dipendente.setNomeUtente(rs.getString("nome_utente"));
                dipendente.setDataNascitaUtente(rs.getDate("data_nascita").toLocalDate());
                dipendente.setCognomeUtente(rs.getString("cognome"));
                dipendenti.add(dipendente);
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return dipendenti;
    }

    /**
     * Restituisce le richieste affiliazione da valutare per la propria azienda.
     * Cercherà le richieste connection valore "data_accettazione" = null.
     * @param idAzienda
     * @return
     */
    public List<RichiestaAffiliazioneAzienda> getRichiesteAffiliazioneDaValutare(Integer idAzienda){
        String query="SELECT * FROM richiesta_affiliazione_azienda WHERE id_azienda=? AND accettata=? AND data_accettazione=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idAzienda);
            st.setBoolean(2,false);
            st.setDate(3,null);
            ResultSet rs = st.executeQuery();

            List<RichiestaAffiliazioneAzienda> richiesteAffiliazione=new ArrayList<>();

            while(rs.next()){
                RichiestaAffiliazioneAziendaProxy richiesta = creaRichiestaAffiliazioneAziendaProxy();
                richiesta.setIdUtente(rs.getInt("id_utente"));
                richiesta.setIdAzienda(rs.getInt("id_azienda"));
                richiesteAffiliazione.add(richiesta);
            }

            return richiesteAffiliazione;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
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
        String query="UPDATE richiesta_affiliazione_azienda SET accettata=?,data_accettazione=CURRENT_DATE " +
        "WHERE id_azienda=? AND id_dipendente=?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setBoolean(1,accettata);
            st.setInt(2,idAzienda);
            st.setInt(3,idDipendente);
            return st.executeUpdate()>0;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Funzione che restituisce l'id dell'azienda affiliata dell'utente idUtente
     * @param idUtente
     * @return
     */
    public Integer getIdAziendaDipendente(Integer idUtente) {

        String query = "SELECT id_azienda FROM richiesta_affiliazione_azienda WHERE accettata = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, true);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getInt("id_azienda");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Integer getNumRichiesteAffiliazione(Integer idAzienda) {
        String query = "SELECT COUNT(*) as somma FROM richiesta_affiliazione_azienda WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getInt("somma");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private DipendenteProxy creaDipendenteProxy(){
        return new DipendenteProxy(
                new RichiestaAffiliazioneAziendaDAO(connection),
                new CredenzialiDAO(connection),
                new RichiestaNoleggioDAO(connection)
        );
    }
    private RichiestaAffiliazioneAziendaProxy creaRichiestaAffiliazioneAziendaProxy(){
        return new RichiestaAffiliazioneAziendaProxy(new UtenteDAO(connection));
    }
}