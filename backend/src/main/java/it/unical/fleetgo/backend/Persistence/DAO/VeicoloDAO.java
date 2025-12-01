package it.unical.fleetgo.backend.Persistence.DAO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.Entity.Veicolo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VeicoloDAO {

    private final Connection connection;

    public VeicoloDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean aggiungiVeicolo(VeicoloDTO veicoloDTO) throws SQLException {
        String query = "INSERT INTO veicolo(targa, immagine_veicolo, modello_veicolo, tipo_distribuzione_veicolo, livello_carburante_veicolo, status_condizione_veicolo) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query))  {
            ps.setString(1, veicoloDTO.getTargaVeicolo());
            ps.setString(2, veicoloDTO.getUrlImmagine());
            ps.setString(3, veicoloDTO.getModello());
            ps.setString(4, veicoloDTO.getTipoDistribuzioneVeicolo());
            ps.setInt(5, veicoloDTO.getLivelloCarburante());
            ps.setString(6, veicoloDTO.getStatusCondizioneVeicolo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminaVeicolo(Integer idVeicolo) {
        String query = "DELETE FROM veicolo WHERE id_veicolo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idVeicolo);
            return ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Veicolo> getVeicoliDisponibiliInPiattaforma() {
        String query = "SELECT * FROM veicolo";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            List<Veicolo> veicoli = new ArrayList<>();

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                veicoli.add(getVeicoloDaResultSet(rs));
            }
            return veicoli;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getIdVeicoloDaDettagli(Veicolo veicolo) {
        String query = "SELECT id_veicolo FROM veicolo WHERE targa = ? AND modello_veicolo = ? AND tipo_distribuzione_veicolo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, veicolo.getTargaVeicolo());
            ps.setString(2, veicolo.getModello());
            ps.setString(3, veicolo.getTipoDistribuzioneVeicolo());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getInt("id_veicolo");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Veicolo getVeicoloDaId(Integer idVeicolo) {
        String query = "SELECT * FROM veicolo WHERE id_veicolo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idVeicolo);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return getVeicoloDaResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean cambiaStatusVeicolo(String nuovoStatus, Integer idVeicolo) {
        String query = "UPDATE veicolo SET status_condizione_veicolo = ? WHERE id_veicolo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nuovoStatus);
            ps.setInt(2, idVeicolo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean cambiaLivelloBenzina(Integer nuovoLivelloBenzina, Integer idVeicolo) {
        String query = "UPDATE veicolo SET livello_carburante_veicolo = ? WHERE id_veicolo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, nuovoLivelloBenzina);
            ps.setInt(2, idVeicolo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Veicolo getVeicoloDaResultSet(ResultSet rs) throws SQLException {
        Veicolo v = new Veicolo();
        v.setIdVeicolo(rs.getInt("id_veicolo"));
        v.setTargaVeicolo(rs.getString("targa"));
        v.setUrlImmagine(rs.getString("immagine_veicolo"));
        v.setModello(rs.getString("modello_veicolo"));
        v.setTipoDistribuzioneVeicolo(rs.getString("tipo_distribuzione_veicolo"));
        v.setLivelloCarburante(rs.getInt("livello_carburante_veicolo"));
        v.setStatusCondizioneVeicolo(rs.getString("status_condizione_veicolo"));
        return v;
    }
}
