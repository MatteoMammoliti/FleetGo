package it.unical.fleetgo.backend.Persistence.DAO;
import it.unical.fleetgo.backend.Persistence.Entity.ContenitoreCredenziali;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;

import static java.sql.Types.NULL;

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
        String pwcriptata= BCrypt.hashpw(password,BCrypt.gensalt(12));

        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, idUtente);
            st.setString(2, pwcriptata);
            st.setString(3, email);
            st.setString(4, urlImmagine);
            return st.executeUpdate()>0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Integer confrontaCredenzialiUtente(String email,String password) throws SQLException{
        String query="SELECT * FROM credenziali_utente WHERE email=?";

        try(PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String passcript = rs.getString("password");

                if (!BCrypt.checkpw(password, passcript)) return null;

                return rs.getInt("id_utente");
            }
            return null;
        }
    }

    /**
     * Restituisce un contenitore con all'interno Email, conferma di patente accettata e urlImmagine patente(null se non presente).
     * @param idUtente
     * @return
     */
    public ContenitoreCredenziali getCredenzialiUtente(Integer idUtente){
        String query = "SELECT email,patente,immagine_patente FROM credenziali_utente WHERE id_utente=?";

        try(PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, idUtente);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                ContenitoreCredenziali contenitore = new ContenitoreCredenziali();
                contenitore.setEmail(rs.getString("email"));
                contenitore.setPatenteAccetta(rs.getBoolean("patente"));
                String urlImmagine = rs.getString("immagine_patente");

                if(urlImmagine!=null) contenitore.setUrlImmagine(urlImmagine);
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

            int righe = st.executeUpdate();

            if(righe == 0) {
                throw new IllegalArgumentException("Email non trovata");
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void modificaPassword(Integer codiceOTP, String nuovaPassword, String email) {

        String selOTP = "SELECT codice_otp, scadenza_codice_otp FROM credenziali_utente WHERE email=?";
        try(PreparedStatement st = conn.prepareStatement(selOTP)) {
            st.setString(1, email);

            ResultSet rs = st.executeQuery();

            if(rs.next()) {
                int codice = rs.getInt("codice_otp");
                LocalDateTime scadenza = rs.getTimestamp("scadenza_codice_otp").toLocalDateTime();

                if(codice != codiceOTP || scadenza.isBefore(LocalDateTime.now())) {
                    throw new RuntimeException("Codice OTP errato o scaduto");
                }

                String passwordCifrata = BCrypt.hashpw(nuovaPassword, BCrypt.gensalt(12));
                String query = "UPDATE credenziali_utente SET password=?, codice_otp = NULL, scadenza_codice_otp = NULL WHERE email=?";

                try(PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, passwordCifrata);
                    ps.setString(2, email);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}