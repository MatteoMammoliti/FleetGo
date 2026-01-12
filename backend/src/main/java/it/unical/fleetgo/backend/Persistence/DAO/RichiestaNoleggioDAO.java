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

    private RichiestaNoleggio estraiRichiestaNoleggio(ResultSet rs) throws SQLException {
        RichiestaNoleggioProxy r = new RichiestaNoleggioProxy(new UtenteDAO(connection), new VeicoloDAO(connection));
        r.setIdRichiestaNoleggio(rs.getInt("id_richiesta"));
        r.setIdUtente(rs.getInt("id_dipendente"));
        r.setIdAzienda(rs.getInt("id_azienda"));
        r.setIdVeicolo(rs.getInt("id_veicolo"));
        r.setOraInizio(rs.getTime("ora_inizio").toLocalTime().toString());
        r.setOraFine(rs.getTime("ora_fine").toLocalTime().toString());
        r.setDataRitiro(rs.getDate("data_ritiro").toLocalDate().toString());
        r.setDataConsegna(rs.getDate("data_consegna").toLocalDate().toString());
        r.setMotivazione(rs.getString("motivazione"));
        r.setCosto(rs.getFloat("costo_noleggio"));
        r.setRichiestaAccettata(rs.getBoolean("accettata"));
        r.setRichiestaAnnullata(rs.getBoolean("richiesta_annullata"));
        r.setStatoRichiesta(rs.getString("stato_richiesta"));

        try {
            r.setNomeLuogo(rs.getString("nome_luogo"));
        } catch (SQLException ignored) {}
        return r;
    }

    public boolean aggiungiRichiestaNoleggio(RichiestaNoleggioDTO richiesta, Double costoNoleggio) {

        if (esisteConflitto(richiesta)) {
            return false;
        }

        String query = "INSERT INTO richiesta_noleggio (id_dipendente, id_azienda, ora_inizio, ora_fine, data_ritiro, data_consegna, motivazione, id_veicolo, costo_noleggio) VALUES (?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement st = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, richiesta.getIdDipendente());
            st.setInt(2, richiesta.getIdAziendaRiferimento());
            st.setTime(3, Time.valueOf(LocalTime.parse(richiesta.getOraInizio())));
            st.setTime(4, Time.valueOf(LocalTime.parse(richiesta.getOraFine())));
            st.setDate(5, Date.valueOf(LocalDate.parse(richiesta.getDataRitiro())));
            st.setDate(6, Date.valueOf(LocalDate.parse(richiesta.getDataConsegna())));
            st.setString(7, richiesta.getMotivazione());
            st.setInt(8, richiesta.getIdVeicolo());
            st.setDouble(9, costoNoleggio);

            st.executeUpdate();
            ResultSet keys = st.getGeneratedKeys();
            return keys.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean esisteConflitto(RichiestaNoleggioDTO r) {
        LocalDateTime inizio = LocalDateTime.parse(r.getDataRitiro() + "T" + r.getOraInizio());
        LocalDateTime fine = LocalDateTime.parse(r.getDataConsegna() + "T" + r.getOraFine());

        String query = "SELECT COUNT(*) FROM richiesta_noleggio WHERE id_dipendente=? AND richiesta_annullata=false " +
                "AND stato_richiesta!='Terminata' AND ((data_ritiro + ora_inizio) < ? AND (data_consegna + ora_fine) > ?)";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, r.getIdDipendente());
            st.setObject(2, Timestamp.valueOf(fine));
            st.setObject(3, Timestamp.valueOf(inizio));
            ResultSet rs = st.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean rimuoviRichiestaNoleggio(Integer idRichiesta) {
        String query = "DELETE FROM richiesta_noleggio WHERE id_richiesta=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, idRichiesta);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean accettaRichiestaNoleggio(Integer idRichiesta) {
        String query = "UPDATE richiesta_noleggio SET accettata=?, stato_richiesta=? WHERE id_richiesta=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setBoolean(1, true);
            st.setString(2, "Da ritirare");
            st.setInt(3, idRichiesta);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean rifiutaRichiestaNoleggio(Integer idRichiesta) {
        String query = "UPDATE richiesta_noleggio SET richiesta_annullata = true WHERE id_richiesta=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, idRichiesta);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void aggiornaStatiNoleggi() {
        LocalDateTime adesso = LocalDateTime.now();
        String q1 = "UPDATE richiesta_noleggio SET stato_richiesta = 'In corso' WHERE stato_richiesta = 'Da ritirare' AND (data_ritiro + ora_inizio) <= ?";
        String q2 = "UPDATE richiesta_noleggio SET stato_richiesta = 'Terminata' WHERE stato_richiesta = 'In corso' AND (data_consegna + ora_fine) <= ?";
        String q3="UPDATE richiesta_noleggio SET richiesta_annullata=true WHERE stato_richiesta='In attesa' AND richiesta_annullata=false AND accettata=false AND (data_ritiro + ora_inizio) <= ?";

        try (PreparedStatement st1 = connection.prepareStatement(q1);
             PreparedStatement st2 = connection.prepareStatement(q2);
             PreparedStatement st3 = connection.prepareStatement(q3);) {
            st1.setObject(1, adesso);
            st1.executeUpdate();
            st2.setObject(1, adesso);
            st2.executeUpdate();
            st3.setObject(1, adesso);
            st3.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RichiestaNoleggio getRichiestaNoleggioById(Integer idRichiesta) {
        aggiornaStatiNoleggi();
        String query = "SELECT * FROM richiesta_noleggio WHERE id_richiesta = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idRichiesta);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? estraiRichiestaNoleggio(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RichiestaNoleggio> getRichiesteDaAccettare(Integer idAzienda) {
        aggiornaStatiNoleggi();
        String query = "SELECT * FROM richiesta_noleggio WHERE id_azienda=? AND accettata=false AND richiesta_annullata=false AND stato_richiesta='In attesa'";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();
            List<RichiestaNoleggio> list = new ArrayList<>();
            while (rs.next()) list.add(estraiRichiestaNoleggio(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getNumRichiesteNoleggioDaAccettare(Integer idAzienda) {
        String query = "SELECT COUNT(*) FROM richiesta_noleggio WHERE id_azienda=? AND accettata=false AND richiesta_annullata=false AND stato_richiesta='In attesa'";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RichiestaNoleggio> getRichiesteNoleggioAccettateByIdDipendente(Integer idDipendente, Integer idAzienda) {
        aggiornaStatiNoleggi();
        String query = "SELECT * FROM richiesta_noleggio WHERE id_dipendente=? AND accettata=true AND id_azienda = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idDipendente);
            ps.setInt(2, idAzienda);
            ResultSet rs = ps.executeQuery();
            List<RichiestaNoleggio> list = new ArrayList<>();
            while (rs.next()) list.add(estraiRichiestaNoleggio(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RichiestaNoleggio> getRichiesteNoleggioAccettateByIdAzienda(Integer idAzienda) {
        aggiornaStatiNoleggi();

        String query = "SELECT * FROM richiesta_noleggio WHERE id_azienda=? AND accettata=true AND richiesta_annullata=false";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, idAzienda);
            ResultSet rs = st.executeQuery();
            List<RichiestaNoleggio> list = new ArrayList<>();
            while (rs.next()) list.add(estraiRichiestaNoleggio(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RichiestaNoleggio getProssimaRichiestaNoleggioDipendente(Integer idDipendente, Integer idAzienda) {
        aggiornaStatiNoleggi();
        String query = "SELECT * FROM richiesta_noleggio WHERE id_dipendente=? AND id_azienda=? AND richiesta_annullata=false AND stato_richiesta != 'Terminata' ORDER BY data_ritiro ASC LIMIT 1";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, idDipendente);
            st.setInt(2, idAzienda);
            ResultSet rs = st.executeQuery();
            return rs.next() ? estraiRichiestaNoleggio(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RichiestaNoleggio> getRichiesteNoleggioDipendente(Integer idDipendente, Integer idAzienda) {

        aggiornaStatiNoleggi();

        String query = "SELECT rn.*, l.nome_luogo FROM richiesta_noleggio rn " +
                "JOIN gestione_veicolo_azienda g ON rn.id_azienda=g.id_azienda AND rn.id_veicolo=g.id_veicolo " +
                "JOIN luogo_azienda l ON l.id_luogo = g.luogo_ritiro_consegna " +
                "WHERE rn.id_dipendente=? AND rn.id_azienda=?";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, idDipendente);
            st.setInt(2, idAzienda);
            ResultSet rs = st.executeQuery();
            List<RichiestaNoleggio> list = new ArrayList<>();
            while (rs.next()) list.add(estraiRichiestaNoleggio(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public StatisticheDipendenteDTO getStatisticheDipendente(Integer idDipendente, Integer idAzienda) {
        String query = "SELECT " +
                "(SELECT COUNT(*) FROM richiesta_noleggio WHERE id_dipendente = ? AND id_azienda=? AND stato_richiesta = 'Terminata' AND EXTRACT(MONTH FROM data_ritiro) = EXTRACT(MONTH FROM CURRENT_DATE) AND EXTRACT(YEAR FROM data_ritiro) = EXTRACT(YEAR FROM CURRENT_DATE)) as viaggi_mese, " +
                "(SELECT COALESCE(SUM(EXTRACT(EPOCH FROM ((data_consegna + ora_fine) - (data_ritiro + ora_inizio))) / 3600), 0) " +
                " FROM richiesta_noleggio WHERE id_dipendente = ? AND id_azienda=? AND stato_richiesta = 'Terminata') as ore_totali";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, idDipendente);
            st.setInt(2, idAzienda);
            st.setInt(3, idDipendente);
            st.setInt(4, idAzienda);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new StatisticheDipendenteDTO(rs.getInt("viaggi_mese"), rs.getFloat("ore_totali"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean controlloRichiestaInCorsoDipendente(Integer idDipendente) {
        String query = "SELECT 1 FROM richiesta_noleggio WHERE id_dipendente=? AND richiesta_annullata=false AND (stato_richiesta='In corso' OR stato_richiesta='Da ritirare')";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, idDipendente);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminaRichiesteNoleggioDipendenteEliminato(Integer idDipendente, Integer idAzienda) {
        String query = "DELETE FROM richiesta_noleggio WHERE id_dipendente=? AND id_azienda=? AND stato_richiesta='In attesa'";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, idDipendente);
            st.setInt(2, idAzienda);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getRichiesteAccettateEInCorso(Integer idAzienda) {
        String query = "SELECT * FROM richiesta_noleggio WHERE id_azienda = ? AND richiesta_annullata = false AND (stato_richiesta = 'In corso' OR stato_richiesta = 'Da ritirare') LIMIT 1";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getRichiesteAccettateEInCorsoPerVeicolo(Integer idAzienda, Integer idVeicolo) {
        String query = "SELECT * FROM richiesta_noleggio WHERE id_veicolo = ? AND id_azienda = ? AND richiesta_annullata = false AND (stato_richiesta = 'In corso' OR stato_richiesta = 'Da ritirare') LIMIT 1";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idVeicolo);
            ps.setInt(2, idAzienda);

            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminaRichiesteInAttesa(Integer idAzienda) {
        String query = "DELETE FROM richiesta_noleggio WHERE id_azienda = ? AND stato_richiesta = 'In attesa' AND richiesta_annullata = false";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void eliminaRichiesteInAttesaPerVeicolo(Integer idAzienda, Integer idVeicolo) {
        String query = "DELETE FROM richiesta_noleggio WHERE id_azienda = ? AND id_veicolo = ? AND stato_richiesta = 'In attesa' AND richiesta_annullata = false";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ps.setInt(2, idVeicolo);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Integer getVeicoliNoleggiatiByIdAzienda(Integer idAzienda) {
        this.aggiornaStatiNoleggi();
        String query = "SELECT COUNT(*) as numero_noleggi FROM richiesta_noleggio WHERE id_azienda = ? AND richiesta_annullata = false AND stato_richiesta=?";

        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, idAzienda);
            ps.setString(2,"In corso");
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getInt("numero_noleggi");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Float getSpesaMensileAzienda(Integer idAzienda) {
        String query = "SELECT SUM(costo_noleggio) as spesa_totale FROM richiesta_noleggio " +
                " WHERE accettata = true AND EXTRACT(year FROM data_consegna) = ?" +
                " AND EXTRACT(month FROM data_consegna) = ? AND id_azienda = ? GROUP BY EXTRACT(year FROM data_consegna), EXTRACT(month FROM data_consegna)";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, LocalDate.now().getYear());
            ps.setInt(2, LocalDate.now().getMonthValue());
            ps.setInt(3, idAzienda);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getFloat("spesa_totale");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}