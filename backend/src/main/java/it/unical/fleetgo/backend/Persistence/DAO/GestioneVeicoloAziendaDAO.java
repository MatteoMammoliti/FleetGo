package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.ContenitoreStatisticheNumeriche;
import it.unical.fleetgo.backend.Models.DTO.GestioneVeicoloAziendaDTO;
import it.unical.fleetgo.backend.Models.Proxy.GestioneVeicoloAziendaProxy;
import it.unical.fleetgo.backend.Persistence.Entity.GestioneVeicoloAzienda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestioneVeicoloAziendaDAO {

    private final Connection connection;

    public GestioneVeicoloAziendaDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserisciNuovoVeicoloGestito(GestioneVeicoloAziendaDTO gestioneVeicoloAzienda) {
        String query = "INSERT INTO gestione_veicolo_azienda(id_veicolo, id_azienda) VALUES (?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, gestioneVeicoloAzienda.getIdVeicolo());
            ps.setInt(2, gestioneVeicoloAzienda.getIdAzienda());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminaVeicoloGestito(GestioneVeicoloAziendaDTO gestioneVeicoloAzienda) {
        String query = "DELETE FROM gestione_veicolo_azienda WHERE id_veicolo = ? AND id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, gestioneVeicoloAzienda.getIdVeicolo());
            ps.setInt(2, gestioneVeicoloAzienda.getIdAzienda());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<GestioneVeicoloAzienda> getVeicoliInGestioneAzienda(Integer idAzienda) {
        String query = "SELECT * FROM gestione_veicolo_azienda WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();
            List<GestioneVeicoloAzienda> gestioni = new ArrayList<>();
            while(rs.next()) {
                GestioneVeicoloAzienda gestione = new GestioneVeicoloAziendaProxy(new VeicoloDAO(connection),new LuogoAziendaDAO(connection));
                gestione.setIdVeicolo(rs.getInt("id_veicolo"));
                gestione.setIdAzienda(rs.getInt("id_azienda"));
                gestione.setIdLuogo(rs.getInt("id_luogo"));
                gestioni.add(gestione);
            }
            return gestioni;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ContenitoreStatisticheNumeriche getStatoVeicoli(Integer idAzienda) {
        String query = "SELECT " +
                "SUM(CASE WHEN v.status_condizione_veicolo = 'Noleggiato' THEN 1 ELSE 0 END) as veicoliNoleggiati," +
                "SUM(CASE WHEN v.status_condizione_veicolo = 'In manutenzione' THEN 1 ELSE 0 END) as veicoliInManutenzione," +
                "SUM(CASE WHEN v.status_condizione_veicolo = 'Libero' THEN 1 ELSE 0 END) as veicoliDisponibili " +
                "FROM veicolo v JOIN gestione_veicolo_azienda g ON g.id_veicolo = v.id_veicolo WHERE g.id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new ContenitoreStatisticheNumeriche(
                        rs.getInt("veicoliNoleggiati"),
                        rs.getInt("veicoliDisponibili"),
                        rs.getInt("veicoliInManutenzione")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void contrassegnaVeicoliLiberiPreEliminazioneAzienda(Integer idAdminAzienda) {
        String query="UPDATE veicolo SET status_condizione_veicolo =? WHERE id_veicolo IN " +
                " (SELECT g.id_veicolo FROM gestione_veicolo_azienda g " +
                "JOIN azienda a ON a.id_azienda=g.id_azienda WHERE a.id_admin_azienda=?)" +
                " AND status_condizione_veicolo != 'Manutenzione'";

        try(PreparedStatement st = connection.prepareStatement(query)){
                st.setString(1, "Libero");
                st.setInt(2, idAdminAzienda);
                st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}