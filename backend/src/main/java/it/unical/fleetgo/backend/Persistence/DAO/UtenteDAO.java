package it.unical.fleetgo.backend.Persistence.DAO;
import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;
import it.unical.fleetgo.backend.Models.Proxy.AdminAziendaleProxy;
import it.unical.fleetgo.backend.Models.Proxy.DipendenteProxy;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.AdminAziendale;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        try (PreparedStatement st = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            st.setString(1,utente.getNomeUtente());
            st.setString(2,utente.getCognomeUtente());
            st.setDate(3, Date.valueOf(utente.getDataNascitaUtente()));
            st.setString(4,utente.getTipoUtente());

            int righe = st.executeUpdate();
            if(righe==0)return null;

            ResultSet rs = st.getGeneratedKeys();
            if(rs.next()) return rs.getInt(1);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    /**
     * Elimina l'utente associato ad idUtente.
     *
     * @param idUtente
     */
    public void eliminaUtente(Integer idUtente){
        String query="DELETE FROM Utente WHERE id_utente=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * Dato un id Restituisce il DIPENDENTE associato.
     * @param idUtente
     * @return ritorna il proxy del dipendente, null se non esiste.
     */
    public Dipendente getDipendenteById(Integer idUtente){
        String query="SELECT * FROM utente WHERE id_utente=? AND tipo_utente=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setString(2,"Dipendente");
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                Dipendente dipendente = new DipendenteProxy(new RichiestaAffiliazioneAziendaDAO(con),new CredenzialiDAO(con));
                dipendente.setNomeUtente(rs.getString("nome_utente"));
                dipendente.setCognomeUtente(rs.getString("cognome"));
                dipendente.setIdUtente(rs.getInt("id_utente"));
                dipendente.setDataNascitaUtente(rs.getDate("data_nascita").toLocalDate());
                return dipendente;
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
    /**
     * Dato un id Restituisce l'ADMIN AZIENDALE associato.
     * @param idUtente
     * @return ritorna il proxy dell'admin aziendale, null se non esiste.
     */
    public AdminAziendale getAdminAziendaDaId(Integer idUtente){
        String query="SELECT * FROM utente WHERE id_utente=? AND tipo_utente=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idUtente);
            st.setString(2,"AdminAziendale");
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                AdminAziendale adminAziendaleProxy = new AdminAziendaleProxy(new AziendaDAO(con));
                adminAziendaleProxy.setNomeUtente(rs.getString("nome_utente"));
                adminAziendaleProxy.setCognomeUtente(rs.getString("cognome"));
                adminAziendaleProxy.setIdUtente(rs.getInt("id_utente"));
                adminAziendaleProxy.setDataNascitaUtente(rs.getDate("data_nascita").toLocalDate());
                return adminAziendaleProxy;
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Funzione che restituisce true se trova che l'email passata come parametro esiste già
     * @param email
     * @return
     */
    public boolean esisteEmail(String email) throws RuntimeException {
        String query = "SELECT * FROM credenziali_utente WHERE email = ?";

        try(PreparedStatement st = con.prepareStatement(query)){
            st.setString(1,email);

            ResultSet rs = st.executeQuery();
            return rs.next();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Integer getAziendaAssociataDipendente(Integer idUtente) throws SQLException{
        String query="SELECT id_azienda FROM richiesta_affiliazione_azienda WHERE id_dipendente=? AND accettata=true";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idUtente);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getInt("id_azienda");
            }
        }
        return null;
    }

    public String getRuoloDaId(Integer idUtente){
        String query = "SELECT tipo_utente FROM utente WHERE id_utente=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idUtente);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getString("tipo_utente");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public void modificaDatiUtente(ModificaDatiUtenteDTO dati) throws RuntimeException, SQLException {
        try{
            con.setAutoCommit(false);
            if(dati.getNome()!=null || dati.getCognome() !=null || dati.getData()!=null) {
                StringBuilder aggiornoUtente=new StringBuilder("UPDATE utente SET ");
                List<Object> parametri = new ArrayList<>();
                boolean primo=true;
                if(dati.getNome()!=null){
                    aggiornoUtente.append("nome_utente=? ");
                    parametri.add(dati.getNome());
                    primo=false;
                }
                if(dati.getCognome()!=null){
                    if(!primo){aggiornoUtente.append(", ");}
                    aggiornoUtente.append("cognome=? ");
                    parametri.add(dati.getCognome());
                    primo=false;
                }
                if(dati.getData()!=null){
                    if(!primo){aggiornoUtente.append(", ");}
                    aggiornoUtente.append("data_nascita=? ");
                    parametri.add(LocalDate.parse(dati.getData()));
                }
                aggiornoUtente.append("WHERE id_utente=? ");
                parametri.add(dati.getIdUtente());

                try(PreparedStatement st = con.prepareStatement(aggiornoUtente.toString())){
                    for(int i=0;i<parametri.size();i++){
                        st.setObject(i+1,parametri.get(i));
                    }
                    if(st.executeUpdate()==0){
                        con.rollback();
                        return;
                    }

                }
            }
            if(dati.getEmail()!=null){
                try(PreparedStatement st = con.prepareStatement("UPDATE credenziali_utente SET email=? WHERE id_utente=?")){
                    st.setString(1,dati.getEmail());
                    st.setInt(2,dati.getIdUtente());
                    if(st.executeUpdate()==0){
                        con.rollback();
                        return;
                    }
                }
            }
            if(dati.getNomeAzienda()!=null || dati.getPIva()!=null){
                StringBuilder aggiornoAzienda=new StringBuilder("UPDATE azienda SET ");
                List<Object> parametri = new ArrayList<>();
                boolean primo= true;
                if(dati.getNomeAzienda()!=null){
                    aggiornoAzienda.append("nome_azienda=? ");
                    parametri.add(dati.getNomeAzienda());
                    primo=false;
                }
                if(dati.getPIva()!=null){
                    if(!primo){aggiornoAzienda.append(", ");}
                    aggiornoAzienda.append("p_iva=? ");
                    parametri.add(dati.getPIva());
                }
                aggiornoAzienda.append("WHERE id_admin_azienda=?");
                parametri.add(dati.getIdUtente());
                try(PreparedStatement st = con.prepareStatement(aggiornoAzienda.toString())){
                    for (int i=0;i<parametri.size();i++){
                        st.setObject(i+1,parametri.get(i));
                    }
                    if(st.executeUpdate()==0){
                        con.rollback();
                        return;
                    }
                }
            }
            con.commit();
        } catch (Exception e) {
            con.rollback();
            if(e instanceof SQLException sqlEccezione){
                String state = sqlEccezione.getSQLState();
                String msg = sqlEccezione.getMessage().toLowerCase();
                if("23505".equals(state)){
                    if(msg.contains("email")){
                        throw new RuntimeException("Email già presente");
                    }
                    else if(msg.contains("p_iva")){
                        throw new RuntimeException("P.Iva già registrata da un'altra azienda");
                    }
                }
            }
            throw new RuntimeException(e);
        }finally {
            con.setAutoCommit(true);
        }
    }

    public ModificaDatiUtenteDTO getDatiUtente(Integer idUtente){
        String query="SELECT c.email,u.nome_utente,u.cognome,u.data_nascita,a.nome_azienda,l.nome_luogo,a.p_iva FROM utente u JOIN" +
                " credenziali_utente c ON u.id_utente=c.id_utente LEFT JOIN azienda a ON u.id_utente=a.id_admin_azienda LEFT JOIN luogo_azienda l ON a.sede_azienda = l.id_luogo WHERE u.id_utente =?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idUtente);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return new ModificaDatiUtenteDTO(rs.getString("nome_utente"),
                        rs.getString("cognome"),
                        rs.getDate("data_nascita").toString(),
                        rs.getString("email"),
                        rs.getString("nome_azienda"),
                        rs.getString("nome_luogo"),
                        rs.getString("p_iva"),
                        null);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Funzione che restituisce le statistiche numeriche utilizzate nelle card visibili
     * all'interno della dashbaord fleetGo
     * @return Verranno restituiti il numero di veicoli totali, veicolo assegnati, in manutenzione e numero di aziende affiliate.
     */
    public ContenitoreStatisticheNumeriche getStatisticheNumeriche() {
        String query = "SELECT " +
                " (SELECT COUNT(*) FROM veicolo) as veicoli_totali, " +
                " (SELECT COUNT (*) FROM azienda) as aziende_totali, " +
                " (SELECT COUNT (*) FROM veicolo WHERE in_manutenzione=true) as veicoli_in_manutenzione," +
                " (SELECT COUNT (*) FROM veicolo WHERE status_contrattuale='Noleggiato') as veicoli_assegnati," +
                " (SELECT COUNT (*) FROM veicolo WHERE status_contrattuale='Disponibile') as veicoli_disponibili, " +
                " (SELECT COUNT (*) FROM view_fatture_da_generare) as fatture_da_generare , " +
                " (SELECT SUM(costo) FROM fattura WHERE fattura_pagata=true AND mese_fattura=? AND anno_fattura =?) as guadagno_mensile";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,LocalDate.now().getMonthValue());
            st.setInt(2,LocalDate.now().getYear());
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return new ContenitoreStatisticheNumeriche(
                        rs.getInt("veicoli_totali"),
                        rs.getInt("veicoli_assegnati"),
                        rs.getInt("veicoli_disponibili"),
                        rs.getInt("veicoli_in_manutenzione"),
                        rs.getInt("aziende_totali"),
                        rs.getInt("fatture_da_generare"),
                        rs.getInt("guadagno_mensile")
                );
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getNomeCognomeAdminById(Integer idUtente) {
        String query = "SELECT nome_utente, cognome FROM utente WHERE id_utente = ?";

        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idUtente);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getString("nome_utente") + " " + rs.getString("cognome");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Dipendente getDipendenteDaId(Integer idDipendente) throws SQLException {
        String query="SELECT * FROM utente WHERE id_utente=? AND tipo_utente=?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,idDipendente);
            st.setString(2,"Dipendente");
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                Dipendente dipendente = new Dipendente();
                dipendente.setIdUtente(rs.getInt("id_utente"));
                dipendente.setNomeUtente(rs.getString("nome_utente"));
                dipendente.setCognomeUtente(rs.getString("cognome"));
                dipendente.setDataNascitaUtente(LocalDate.parse(rs.getDate("data_nascita").toString()));
                return dipendente;
            }
        }
        return null;
    }
}