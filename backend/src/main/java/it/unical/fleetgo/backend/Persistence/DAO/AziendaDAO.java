package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.AziendaDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Azienda;
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

    public boolean inserisciAzienda(AziendaDTO azienda){
        String query = "INSERT INTO azienda(id_admin_azienda, sede_azienda, nome_azienda, p_iva) VALUES (?, ?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, azienda.getIdAdminAzienda());
            ps.setString(2, azienda.getSedeAzienda());
            ps.setString(3, azienda.getNomeAzienda());
            ps.setString(4, azienda.getPIva());
            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void eliminaAzienda(Integer idAdminAzienda){
        String query = "DELETE FROM azienda WHERE id_admin_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, idAdminAzienda);
            ps.executeUpdate();
        } catch (SQLException e){
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

    public Azienda getAzienda(Integer idAzienda) {
        String query = "SELECT * FROM azienda WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Azienda a = new Azienda();
                a.setIdAzienda(rs.getInt("id_azienda"));
                a.setSedeAzienda(rs.getString("sede_azienda"));
                a.setNomeAzienda(rs.getString("nome_azienda"));
                a.setPIva(rs.getString("p_iva"));
                return a;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Azienda> getAziendeInPiattaforme() {
        String query = "SELECT * FROM azienda";
        List<Azienda> aziende = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Azienda a = new Azienda();
                a.setIdAzienda(rs.getInt("id_azienda"));
                a.setIdAdmin(rs.getInt("id_admin_azienda"));
                a.setSedeAzienda(rs.getString("sede_azienda"));
                a.setNomeAzienda(rs.getString("nome_azienda"));
                a.setPIva(rs.getString("p_iva"));
                aziende.add(a);
            }
            return aziende;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}