package it.unical.fleetgo.backend.Persistence.DAO;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.CredenzialiUtente;

import java.sql.*;
import java.time.LocalDateTime;

public class CredenzialiDAO {
    Connection conn;

    public CredenzialiDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * crea un record in credenziali per l'id associato. Registra email e password.
     * da usare SUBITO DOPO inserisciUtente.
     * @param idUtente
     * @param email
     * @param password
     * @return
     */
    public boolean creaCredenzialiUtente(Integer idUtente,String email,String password,String urlImmagine){
        String query = "INSERT INTO credenziali_utente (id_utente,password,email,immagine_patente) VALUES (?,?,?,?)";
        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, idUtente);
            st.setString(2, password);
            st.setString(3, email);
            st.setString(4, urlImmagine);
            return st.executeUpdate()>0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Restituisce un contenitore con all'interno Email, conferma di patente accettata e urlImmagine patente(null se non presente).
     * @param idUtente
     * @return
     */
    public CredenzialiUtente getCredenzialiUtenteById(Integer idUtente){
        String query = "SELECT email,immagine_patente FROM credenziali_utente WHERE id_utente=?";

        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, idUtente);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                CredenzialiUtente contenitore = new CredenzialiUtente();
                contenitore.setEmail(rs.getString("email"));
                String urlImmagine = rs.getString("immagine_patente");

                if(urlImmagine != null) contenitore.setImgPatente(urlImmagine);
                return contenitore;
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public CredenzialiUtente getCredenzialiUtenteByEmail(String email){
        CredenzialiUtente contenitore = new CredenzialiUtente();
        String query="SELECT password,id_utente FROM credenziali_utente WHERE email=?";
        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                contenitore.setPassword(rs.getString("password"));
                contenitore.setIdUtente(rs.getInt("id_utente"));
                return contenitore;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public void inserimentoDatiRecuperoPassword(String email, Integer codiceOTP){

        String query = "UPDATE credenziali_utente SET codice_otp=?, scadenza_codice_otp=? WHERE email=?";

        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, codiceOTP);

            LocalDateTime scadenza = LocalDateTime.now().plusMinutes(5);
            st.setTimestamp(2, Timestamp.valueOf(scadenza));
            st.setString(3, email);
            st.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean modificaPassword(Integer codiceOTP, String nuovaPassword, String email) {

        String selOTP = "SELECT codice_otp, scadenza_codice_otp FROM credenziali_utente WHERE email=?";
        try(PreparedStatement st = conn.prepareStatement(selOTP)) {
            st.setString(1, email);

            ResultSet rs = st.executeQuery();

            if(rs.next()) {
                int codice = rs.getInt("codice_otp");
                LocalDateTime scadenza = rs.getTimestamp("scadenza_codice_otp").toLocalDateTime();

                if(codice != codiceOTP || scadenza.isBefore(LocalDateTime.now())) {
                    return false;
                }

                String query = "UPDATE credenziali_utente SET password=?, codice_otp = NULL, scadenza_codice_otp = NULL WHERE email=?";

                try(PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, nuovaPassword);
                    ps.setString(2, email);
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean isPrimoAccessoAdminAziendale(Integer idUtente) {
        String query = "SELECT primo_accesso FROM credenziali_utente WHERE id_utente=?";

        try(PreparedStatement ps = conn.prepareStatement(query)){
            ps.setInt(1, idUtente);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getBoolean("primo_accesso");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean impostaPrimoAccesso(String email) {
        String query = "UPDATE credenziali_utente SET primo_accesso = false WHERE email=?";

        try(PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRuoloByEmail(String email) {
        String query = "SELECT u.tipo_utente FROM utente u JOIN credenziali_utente c ON c.id_utente = u.id_utente WHERE c.email = ?";

        try(PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getString("tipo_utente");
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getEmailAdminAziendale(Integer idAzienda) {
        String query = "SELECT c.email FROM credenziali_utente c JOIN azienda a ON a.id_admin_azienda = c.id_utente WHERE a.id_azienda = ?";

        try(PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getString("email");
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getUrlPatente(Integer idUtente) {
        String query = "SELECT immagine_patente FROM credenziali_utente WHERE id_utente=?";

        try(PreparedStatement ps = conn.prepareStatement(query)){
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getString("immagine_patente");
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateUrlPatente(Integer idUtente, String urlPatente) {
        String query = "UPDATE credenziali_utente SET immagine_patente = ? WHERE id_utente=?";

        try(PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, urlPatente);
            ps.setInt(2, idUtente);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}