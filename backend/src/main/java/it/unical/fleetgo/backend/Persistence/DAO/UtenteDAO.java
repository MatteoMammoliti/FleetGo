package it.unical.fleetgo.backend.Persistence.DAO;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Models.Proxy.AdminAziendaleProxy;
import it.unical.fleetgo.backend.Models.Proxy.DipendenteProxy;
import java.sql.*;

public class UtenteDAO {
    Connection con;

    public UtenteDAO(Connection con){
        this.con=con;
    }

    /**
     * Inserisce il record utente all'interno della tabella utente del DB.
     * Da usare insieme a set credenziali.
     * @param utente
     * @return l'id dell'utente appena inserito o null se l'operazione dovesse non andare a buon fine.
     */
    public Integer inserisciUtente(UtenteDTO utente){
        String query="INSERT INTO Utente (nome_utente,cognome,data_nascita,tipo_utente) VALUES (?,?,?,?)";
        try (PreparedStatement st = con.prepareStatement(query)){
            st.setString(1,utente.getNomeUtente());
            st.setString(2,utente.getCognomeUtente());
            Date formatto = Date.valueOf(utente.getDataNascitaUtente());
            st.setDate(3,formatto);
            st.setString(4,utente.getTipoUtente());
            int righe = st.executeUpdate();
            if(righe==0){return null;}
            ResultSet rs = st.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Elimina l'utente associato ad idUtente.
     * @param idUtente
     * @return
     */
    public boolean eliminaUtente(Integer idUtente){
        String query="DELETE FROM Utente WHERE id_utente=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idUtente);
            return st.executeUpdate()>0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Dato un id Restituisce il DIPENDENTE associato.
     * @param idUtente
     * @return ritorna il proxy del dipendente, null se non esiste.
     */
    public DipendenteProxy getDipendenteDaId(Integer idUtente){
        String query="SELECT * FROM utente WHERE id_utente=? AND tipo_utente=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setString(2,"Dipendente");
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                DipendenteProxy dipendente = creoDipendenteProxy();
                dipendente.setNomeUtente(rs.getString("nome_utente"));
                dipendente.setCognomeUtente(rs.getString("cognome"));
                dipendente.setIdUtente(rs.getInt("id_utente"));
                dipendente.setDataNascitaUtente(rs.getDate("data_nascita").toLocalDate());
                return dipendente;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Dato un id Restituisce l'ADMIN AZIENDALE associato.
     * @param idUtente
     * @return ritorna il proxy dell'admin aziendale, null se non esiste.
     */
    public AdminAziendaleProxy getAdminAziendaDaId(Integer idUtente){
        String query="SELECT * FROM utente WHERE id_utente=? AND tipo_utente=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setString(2,"AdminAziendale");
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                AdminAziendaleProxy adminAziendaleProxy = creoAdminAziendaleProxy();
                adminAziendaleProxy.setNomeUtente(rs.getString("nome_utente"));
                adminAziendaleProxy.setCognomeUtente(rs.getString("cognome"));
                adminAziendaleProxy.setIdUtente(rs.getInt("id_utente"));
                adminAziendaleProxy.setDataNascitaUtente(rs.getDate("data_nascita").toLocalDate());
                return adminAziendaleProxy;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private AdminAziendaleProxy creoAdminAziendaleProxy(){
        return new AdminAziendaleProxy(new AziendaDAO(con),new RichiestaNoleggioDAO(con),new RichiestaAffiliazioneAziendaDAO(con),
                new GestioneVeicoloAziendaDAO(con),new FatturaDAO(con),new LuogoAziendaDAO(con),
                new RichiesteManutenzioneDAO(con));
    }
    private DipendenteProxy creoDipendenteProxy(){
        return new DipendenteProxy(new RichiestaAffiliazioneAziendaDAO(con),new CredenzialiDAO(con),
                new RichiestaNoleggioDAO(con));
    }
}