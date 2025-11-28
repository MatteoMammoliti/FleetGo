package it.unical.fleetgo.backend.Persistence.DAO;
import it.unical.fleetgo.backend.Persistence.Entity.ContenitoreCredenziali;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String urlcriptata = BCrypt.hashpw(urlImmagine,BCrypt.gensalt(12));

        System.out.println("ho ricevuto:" + idUtente + ", " + email + ", " + pwcriptata + ", " + urlImmagine);
        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, idUtente);
            st.setString(2, pwcriptata);
            st.setString(3, email);
            st.setString(4, urlcriptata);
            return st.executeUpdate()>0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Integer confrontaCredenzialiUtente(String email,String password) throws SQLException{
        String query="SELECT * FROM credenziali_utente WHERE email=?";
        try(PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String passcript = rs.getString("password");
                if (!BCrypt.checkpw(password, passcript)) {
                    System.out.println("Password incorrecte");
                    return null;
                }
                System.out.println("login effettuata");
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
                if(urlImmagine!=null){
                    contenitore.setUrlImmagine(urlImmagine);
                };
                return contenitore;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;

    }
}
