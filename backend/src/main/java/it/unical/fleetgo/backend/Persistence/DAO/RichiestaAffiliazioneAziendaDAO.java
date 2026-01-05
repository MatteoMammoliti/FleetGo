package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreDatiAzienda;
import it.unical.fleetgo.backend.Models.Proxy.DipendenteProxy;
import it.unical.fleetgo.backend.Models.Proxy.RichiestaAffiliazioneAziendaProxy;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaAffiliazioneAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RichiestaAffiliazioneAziendaDAO {
    private final Connection connection;

    public RichiestaAffiliazioneAziendaDAO(Connection con) {this.connection =con;}

    public void aggiungiRichiestaAffiliazioneAzienda(Integer idDipendente,Integer idAzienda){
        String query ="INSERT INTO richiesta_affiliazione_azienda (id_dipendente,id_azienda) VALUES (?,?)";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idDipendente);
            st.setInt(2,idAzienda);
            st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void rimuoviRichiestaAffiliazioneAzienda(Integer idDipendente, Integer idAzienda,Boolean affiliato) {
        String query;
        if(affiliato){
            query="DELETE FROM richiesta_affiliazione_azienda WHERE id_dipendente=? AND id_azienda=? AND data_risposta IS NOT NULL";
        }else {
            query="DELETE FROM richiesta_affiliazione_azienda WHERE id_dipendente=? AND id_azienda=? AND data_risposta IS NULL";
        }

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idDipendente);
            st.setInt(2,idAzienda);
            st.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Dipendente> getDipendentiAzienda(Integer idAzienda) throws RuntimeException {
        List<Dipendente> dipendenti=new ArrayList<>();

        String query= "SELECT u.* FROM richiesta_affiliazione_azienda ra JOIN utente u ON ra.id_dipendente = u.id_utente " +
                " WHERE ra.id_azienda = ? AND ra.accettata = ?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idAzienda);
            st.setBoolean(2,true);
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                DipendenteProxy dipendente =new DipendenteProxy(this, new CredenzialiDAO(connection));
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

    public List<RichiestaAffiliazioneAzienda> getRichiesteAffiliazioneDaAccettare(Integer idAzienda){
        String query="SELECT * FROM richiesta_affiliazione_azienda WHERE id_azienda = ? AND accettata = false AND data_risposta IS NULL";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idAzienda);
            ResultSet rs = st.executeQuery();

            List<RichiestaAffiliazioneAzienda> richiesteAffiliazione=new ArrayList<>();

            while(rs.next()){
                RichiestaAffiliazioneAziendaProxy richiesta = new RichiestaAffiliazioneAziendaProxy(new UtenteDAO(connection));
                richiesta.setIdUtente(rs.getInt("id_dipendente"));
                richiesta.setIdAzienda(rs.getInt("id_azienda"));
                richiesteAffiliazione.add(richiesta);
            }

            return richiesteAffiliazione;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void rispondiRichiestaAffiliazione(Integer idAzienda, Integer idDipendente, boolean risposta){
        if(risposta){
            String query="UPDATE richiesta_affiliazione_azienda SET accettata = ?, data_risposta = CURRENT_DATE " +
                    "WHERE id_azienda=? AND id_dipendente=?";
            try(PreparedStatement st = connection.prepareStatement(query)){
                st.setBoolean(1, risposta);
                st.setInt(2,idAzienda);
                st.setInt(3,idDipendente);
                st.executeUpdate();
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println(idDipendente);
            String query="DELETE FROM richiesta_affiliazione_azienda WHERE id_dipendente=? AND id_azienda=?";
            try(PreparedStatement st = connection.prepareStatement(query)){
                st.setInt(1,idDipendente);
                st.setInt(2,idAzienda);
                st.executeUpdate();
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }


    }


    public Integer getIdAziendaDipendente(Integer idUtente) {

        String query = "SELECT id_azienda FROM richiesta_affiliazione_azienda WHERE accettata = ? AND id_dipendente = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, true);
            ps.setInt(2, idUtente);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getInt("id_azienda");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Integer getNumRichiesteAffiliazione(Integer idAzienda) {
        String query = "SELECT COUNT(*) as somma FROM richiesta_affiliazione_azienda WHERE id_azienda = ? AND accettata = false AND data_risposta IS NULL";

        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getInt("somma");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public ContenitoreDatiAzienda getRichiestaInAttesaDipendente(Integer idDipendente) {
        String query="SELECT a.* FROM richiesta_affiliazione_azienda ra JOIN azienda a ON a.id_azienda=ra.id_azienda" +
                " WHERE id_dipendente=? AND data_risposta IS NULL AND accettata=?";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, idDipendente);
            ps.setBoolean(2, false);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ContenitoreDatiAzienda contenitoreDatiAzienda=new ContenitoreDatiAzienda();
                contenitoreDatiAzienda.setIdAzienda(rs.getInt("id_azienda"));
                contenitoreDatiAzienda.setNomeAzienda(rs.getString("nome_azienda"));
                return contenitoreDatiAzienda;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rimuoviDipendentiDaAziendaDisabilitata(Integer idAzienda) {
        String query = "DELETE FROM richiesta_affiliazione_azienda WHERE id_azienda=?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}