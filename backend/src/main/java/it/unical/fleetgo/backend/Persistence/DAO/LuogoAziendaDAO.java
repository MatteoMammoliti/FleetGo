package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Exceptions.LuogoNonEliminabile;
import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LuogoAziendaDAO {

    private final Connection connection;

    public LuogoAziendaDAO(Connection connection) {
        this.connection = connection;
    }

    public Integer inserisciLuogo(LuogoDTO luogoDTO) {
        String query = "INSERT INTO luogo_azienda(id_azienda, nome_luogo, longitudine, latitudine) VALUES (?, ?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, luogoDTO.getIdAzienda());
            ps.setString(2, luogoDTO.getNomeLuogo());
            ps.setDouble(3, luogoDTO.getLongitudine());
            ps.setDouble(4, luogoDTO.getLatitudine());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean rimuoviLuogo(Integer id_luogo) {

        String controlloVeicoliAssociati = "SELECT 1 FROM gestione_veicolo_azienda WHERE luogo_ritiro_consegna = ?";

        try(PreparedStatement ps = connection.prepareStatement(controlloVeicoliAssociati)) {
            ps.setInt(1, id_luogo);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) throw new LuogoNonEliminabile("Impossibile eliminare il luogo poichè ci sono dei veicoli parcheggiati.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "DELETE FROM luogo_azienda WHERE id_luogo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id_luogo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LuogoAzienda> getLuogiDisponibiliPerAzienda(Integer idAzienda) {
        String query = "SELECT * FROM luogo_azienda WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();
            List<LuogoAzienda> luoghi = new ArrayList<>();

            while(rs.next()) {
                LuogoAzienda l = new LuogoAzienda();
                l.setIdLuogo(rs.getInt("id_luogo"));
                l.setIdAzienda(rs.getInt("id_azienda"));
                l.setNomeLuogo(rs.getString("nome_luogo"));
                l.setLongitudine((float) rs.getDouble("longitudine"));
                l.setLatitudine((float) rs.getDouble("latitudine"));
                luoghi.add(l);
            }
            return luoghi;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public LuogoAzienda getLuogoById(Integer idLuogo) {
        String query = "SELECT * from luogo_azienda WHERE id_luogo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idLuogo);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                LuogoAzienda l = new LuogoAzienda();
                l.setIdLuogo(idLuogo);
                l.setIdAzienda(rs.getInt("id_azienda"));
                l.setNomeLuogo(rs.getString("nome_luogo"));
                l.setLongitudine((float) rs.getDouble("longitudine"));
                l.setLatitudine((float) rs.getDouble("latitudine"));
                return l;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}