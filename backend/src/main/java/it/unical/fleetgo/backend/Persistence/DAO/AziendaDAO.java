package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Exceptions.PIVAEsistente;
import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Models.DTO.ContenitoreDatiAzienda;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
import org.jspecify.annotations.NonNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AziendaDAO {

    private final Connection connection;

    public AziendaDAO(Connection connection) {
        this.connection = connection;
    }

    @NonNull
    private List<Azienda> estraiAziendaDaResultSet(String query) {
        List<Azienda> aziende = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Azienda a = new Azienda();
                a.setIdAzienda(rs.getInt("id_azienda"));
                a.setIdAdmin(rs.getInt("id_admin_azienda"));
                a.setSedeAzienda(rs.getInt("sede_azienda"));
                a.setNomeAzienda(rs.getString("nome_azienda"));
                a.setPIva(rs.getString("p_iva"));
                aziende.add(a);
            }
            return aziende;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void inserisciAzienda(AziendaDTO azienda){
        String query = "INSERT INTO azienda(id_admin_azienda, nome_azienda, p_iva) VALUES (?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, azienda.getIdAdminAzienda());
            ps.setString(2, azienda.getNomeAzienda());
            ps.setString(3, azienda.getPIva());
            ps.executeUpdate();
        } catch (SQLException e){
            if (e.getSQLState().equals("23505") && (e.getMessage().contains("p_iva") || e.getMessage().contains("partita_iva"))) {
                throw new PIVAEsistente();
            }
            throw new RuntimeException(e);
        }
    }

    public void gestisciAttivitaAzienda(Integer idAzienda, Boolean status) {
        String query = "UPDATE azienda SET attiva = ? WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, status);
            ps.setInt(2, idAzienda);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getIdAziendaGestita(Integer idAdminAziendale){
        String query = "SELECT id_azienda FROM azienda WHERE id_admin_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAdminAziendale);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getInt("id_azienda");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Azienda getAziendaById(Integer idAzienda) {
        String query = "SELECT * FROM azienda WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Azienda a = new Azienda();
                a.setIdAzienda(rs.getInt("id_azienda"));
                a.setSedeAzienda(rs.getInt("sede_azienda"));
                a.setNomeAzienda(rs.getString("nome_azienda"));
                a.setPIva(rs.getString("p_iva"));
                return a;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getNomeAziendaById(Integer idAzienda) {
        String query = "SELECT nome_azienda FROM azienda WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getString("nome_azienda");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Azienda> getAziendeAttiveInPiattaforme() {
        String query = "SELECT * FROM azienda WHERE attiva = true";
        return estraiAziendaDaResultSet(query);
    }

    public List<Azienda> getAziendeDisabilitateInPiattaforme() {
        String query = "SELECT * FROM azienda WHERE attiva = false";
        return estraiAziendaDaResultSet(query);
    }

    public List<ContenitoreDatiAzienda> getInformazioniAziendeInPiattaforme() throws SQLException{
        List<ContenitoreDatiAzienda> infoAziende = new ArrayList<>();
        String query="SELECT a.*, l.nome_luogo, " +
                " (SELECT COUNT(*) FROM gestione_veicolo_azienda gv WHERE gv.id_azienda=a.id_azienda) as totale_veicolo, " +
                " (SELECT COUNT(*) FROM richiesta_affiliazione_azienda ra WHERE ra.id_azienda=a.id_azienda AND ra.accettata= true) as totale_dipendenti" +
                " FROM azienda a LEFT JOIN luogo_azienda l ON l.id_luogo = a.sede_azienda WHERE a.attiva = true";
        try(PreparedStatement st = connection.prepareStatement(query)){
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                ContenitoreDatiAzienda contenitore=new ContenitoreDatiAzienda();
                contenitore.setIdAzienda(rs.getInt("id_azienda"));
                contenitore.setNomeAzienda(rs.getString("nome_azienda"));
                contenitore.setNomeSedeAzienda(rs.getString("nome_luogo"));
                contenitore.setTotaleDipendentiAzienda(rs.getInt("totale_dipendenti"));
                contenitore.setTotaleVeicoliAzienda(rs.getInt("totale_veicolo"));
                infoAziende.add(contenitore);
            }
            return infoAziende;
        }
    }

    public Boolean impostaSedeAzienda(Integer idLuogo, Integer idAzienda) {
        String query = "UPDATE azienda SET sede_azienda = ? WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idLuogo);
            ps.setInt(2, idAzienda);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAziendaAttiva(Integer idAzienda) {
        String query = "SELECT attiva FROM azienda WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getBoolean("attiva");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean isSedeImpostata(Integer idAzienda) {
        String query = "SELECT 1 FROM azienda WHERE id_azienda = ? AND sede_azienda IS NOT  NULL";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}