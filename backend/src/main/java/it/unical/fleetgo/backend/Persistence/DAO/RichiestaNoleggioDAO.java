package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.DTO.StatisticheDipendenteDTO;
import it.unical.fleetgo.backend.Models.Proxy.RichiestaNoleggioProxy;
import it.unical.fleetgo.backend.Persistence.Entity.RichiestaNoleggio;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RichiestaNoleggioDAO {

    private final Connection connection;

    public RichiestaNoleggioDAO(Connection con) {
        this.connection = con;
    }

    /**
     * Aggiunge una nuova richiesta di noleggio.
     * @param richiestaNoleggio
     * @return restituisce l'id della richiesta, null se qualcosa va storto.
     */
    public Integer aggiungiRichiestaNoleggio(RichiestaNoleggioDTO richiestaNoleggio){
        String query="INSERT INTO richiesta_noleggio (id_dipendente,id_azienda,ora_inizio,ora_fine,data_ritiro, " +
                " data_consegna, motivazione) VALUES (?,?,?,?,?,?,?)";

        String dataInizio=richiestaNoleggio.getDataRitiro();
        String dataFine=richiestaNoleggio.getDataConsegna();
        String oraInizio =richiestaNoleggio.getOraInizio();
        String oraFine =richiestaNoleggio.getOraFine();

        try(PreparedStatement st = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)){
            st.setInt(1,richiestaNoleggio.getIdDipendente());
            st.setInt(2,richiestaNoleggio.getIdAziendaRiferimento());
            st.setTime(3, Time.valueOf( LocalTime.parse(oraInizio)));
            st.setTime(4, Time.valueOf(LocalTime.parse(oraFine)));
            st.setDate(5, Date.valueOf(LocalDate.parse(dataInizio)));
            st.setDate(6, Date.valueOf(LocalDate.parse(dataFine)));
            st.setString(7, richiestaNoleggio.getMotivazione());
            st.executeUpdate();
            ResultSet idGenerato = st.getGeneratedKeys();

            if(idGenerato.next()) return idGenerato.getInt(1);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean rimuoviRichiestaNoleggio(Integer idRichiesta){
        String query="DELETE FROM richiesta_noleggio WHERE id_richiesta=?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idRichiesta);
            return st.executeUpdate()>0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Permette di contrassegnare una richiesta noleggio come accettata o rifiutata
     * in base al valore della variabile booleana accettata.
     * @param idRichiesta
     * @param accettata
     * @return
     */
    public boolean contrassegnaRichiestaNoleggio(Integer idRichiesta,boolean accettata){
        String query="UPDATE richiesta_noleggio SET accettata=? WHERE id_richiesta=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setBoolean(1,accettata);
            st.setInt(2,idRichiesta);
            return st.executeUpdate()>0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<RichiestaNoleggio> getRichiesteNoleggioAziendaDaAccettare(Integer idAzienda){
        try{
            this.aggiornaStatiNoleggi();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        String query="SELECT * FROM richiesta_noleggio WHERE id_azienda=? AND accettata=?";

        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idAzienda);
            st.setBoolean(2,false);
            ResultSet rs = st.executeQuery();

            List<RichiestaNoleggio> richiesteNoleggio=new ArrayList<>();

            while(rs.next()){
                RichiestaNoleggioProxy richiesta=new RichiestaNoleggioProxy(new UtenteDAO(connection),new VeicoloDAO(connection));
                richiesta.setIdRichiestaNoleggio(rs.getInt("id_richiesta"));
                richiesta.setIdUtente(rs.getInt("id_dipendente"));
                richiesta.setOraInizio(rs.getTime("ora_inizio").toLocalTime().toString());
                richiesta.setOraFine(rs.getTime("ora_fine").toLocalTime().toString());
                richiesta.setDataRitiro(rs.getDate("data_ritiro").toLocalDate().toString());
                richiesta.setDataConsegna(rs.getDate("data_consegna").toLocalDate().toString());
                richiesta.setMotivazione(rs.getString("motivazione"));
                richiesta.setRichiestaAccettata(rs.getBoolean("accettata"));
                richiesta.setIdVeicolo(rs.getInt("id_veicolo"));
                richiesteNoleggio.add(richiesta);
            }

            return richiesteNoleggio;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public RichiestaNoleggio getProssimaRichiestaNoleggioDipendente(Integer idDipendente) throws SQLException{
        this.aggiornaStatiNoleggi();
        String query="SELECT * FROM richiesta_noleggio WHERE id_dipendente=? AND richiesta_annullata=false AND stato_richiesta !=?" +
                " ORDER BY data_ritiro ASC LIMIT 1";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idDipendente);
            st.setString(2,"Terminata");
            ResultSet rs = st.executeQuery();
            RichiestaNoleggio richiesta = new  RichiestaNoleggioProxy(new UtenteDAO(connection),new VeicoloDAO(connection));
            this.estraiRichiestaDaResult(rs,richiesta,false);
            return richiesta;
        }
    }

    public StatisticheDipendenteDTO getStatisticheDipendente(Integer idDipendente) throws SQLException{
        String query="SELECT " +
                "(SELECT COUNT(*) FROM richiesta_noleggio WHERE id_dipendente = ? AND stato_richiesta = 'Terminata' AND EXTRACT(MONTH FROM data_ritiro) = EXTRACT(MONTH FROM CURRENT_DATE) " +
                " AND EXTRACT(YEAR FROM data_ritiro) = EXTRACT(YEAR FROM CURRENT_DATE)) as viaggi_mese, " +
                "(SELECT COALESCE(SUM(EXTRACT(EPOCH FROM ((data_consegna + ora_fine) - (data_ritiro + ora_inizio))) / 3600), 0) " +
                " FROM richiesta_noleggio WHERE id_dipendente = ? AND stato_richiesta = 'Terminata') as ore_totali";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idDipendente);
            st.setInt(2,idDipendente);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                StatisticheDipendenteDTO dto = new StatisticheDipendenteDTO(rs.getInt("viaggi_mese"),rs.getFloat("ore_totali"));
                return dto;
            }
        }
        return null;
    }

    public List<RichiestaNoleggio> getRichiesteNoleggioDipendente(Integer idDipendente) throws SQLException{
        this.aggiornaStatiNoleggi();
        String query="SELECT rn.*,l.nome_luogo FROM richiesta_noleggio rn JOIN gestione_veicolo_azienda g ON rn.id_azienda=g.id_azienda " +
                " JOIN luogo_azienda l ON l.id_luogo = g.luogo_ritiro_consegna WHERE rn.id_dipendente=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1,idDipendente);
            ResultSet rs = st.executeQuery();
            List<RichiestaNoleggio> richiesteNoleggio = new ArrayList<>();
            this.estraiRichieseaDaResult(rs,richiesteNoleggio,true);
            return richiesteNoleggio;
        }
    }

    private void aggiornaStatiNoleggi() throws SQLException {
        LocalDateTime adesso = LocalDateTime.now();
        String query1 = "UPDATE richiesta_noleggio SET stato_richiesta = 'In corso' WHERE stato_richiesta = 'Da ritirare' AND (data_ritiro + ora_inizio) <= ?";
        String query2 = "UPDATE richiesta_noleggio SET stato_richiesta = 'Terminata' WHERE stato_richiesta = 'In corso' AND (data_consegna + ora_fine) <= ?";

        try (PreparedStatement st = connection.prepareStatement(query1);
             PreparedStatement st2 = connection.prepareStatement(query2)) {
            st.setObject(1, adesso);
            st.executeUpdate();
            st2.setObject(1, adesso);
            st2.executeUpdate();
        }
    }
    private void estraiRichiestaDaResult(ResultSet rs,RichiestaNoleggio richiesta,boolean conLuogo) throws SQLException {
        if (rs.next()){
            richiesta.setIdRichiestaNoleggio(rs.getInt("id_richiesta"));
            richiesta.setIdUtente(rs.getInt("id_dipendente"));
            richiesta.setIdAzienda(rs.getInt("id_azienda"));
            richiesta.setIdVeicolo(rs.getInt("id_veicolo"));
            richiesta.setOraInizio(rs.getTime("ora_inizio").toLocalTime().toString());
            richiesta.setOraFine(rs.getTime("ora_fine").toLocalTime().toString());
            richiesta.setDataRitiro(rs.getDate("data_ritiro").toLocalDate().toString());
            richiesta.setDataConsegna(rs.getDate("data_consegna").toLocalDate().toString());
            richiesta.setMotivazione(rs.getString("motivazione"));
            richiesta.setRichiestaAccettata(rs.getBoolean("accettata"));
            richiesta.setRichiestaAnnullata(rs.getBoolean("richiesta_annullata"));
            richiesta.setCosto(rs.getFloat("costo_noleggio"));
            richiesta.setStatoRichiesta(rs.getString("stato_richiesta"));
            if(conLuogo){
                richiesta.setNomeLuogo(rs.getString("nome_luogo"));
            }

        }
    }

    private void estraiRichieseaDaResult(ResultSet rs,List<RichiestaNoleggio> richieste,boolean conLuogo) throws SQLException {
        while (rs.next()){
            RichiestaNoleggioProxy ric = new RichiestaNoleggioProxy(new UtenteDAO(connection),new VeicoloDAO(connection));
            ric.setIdRichiestaNoleggio(rs.getInt("id_richiesta"));
            ric.setIdUtente(rs.getInt("id_dipendente"));
            ric.setIdAzienda(rs.getInt("id_azienda"));
            ric.setIdVeicolo(rs.getInt("id_veicolo"));
            ric.setOraInizio(rs.getTime("ora_inizio").toLocalTime().toString());
            ric.setOraFine(rs.getTime("ora_fine").toLocalTime().toString());
            ric.setDataRitiro(rs.getDate("data_ritiro").toLocalDate().toString());
            ric.setDataConsegna(rs.getDate("data_consegna").toLocalDate().toString());
            ric.setMotivazione(rs.getString("motivazione"));
            ric.setRichiestaAccettata(rs.getBoolean("accettata"));
            ric.setRichiestaAnnullata(rs.getBoolean("richiesta_annullata"));
            ric.setCosto(rs.getFloat("costo_noleggio"));
            ric.setStatoRichiesta(rs.getString("stato_richiesta"));
            if(conLuogo){
                ric.setNomeLuogo(rs.getString("nome_luogo"));
            }
            richieste.add(ric);

        }
    }


}