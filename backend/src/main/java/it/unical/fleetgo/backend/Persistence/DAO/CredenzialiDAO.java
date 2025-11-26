package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreCredenzialiDTO;
import it.unical.fleetgo.backend.Persistence.Entity.ContenitoreCredenziali;

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
    public boolean creaCredenzialiUtente(Integer idUtente,String email,String password){
        String query = "INSERT INTO credenziali_utente (id_utente,password,email) VALUES (?,?,?)";
        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setInt(1, idUtente);
            st.setString(2, password);
            st.setString(3, email);
            return st.executeUpdate()>0;
                }catch (SQLException e){
            e.printStackTrace();
            }
        return false;
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
