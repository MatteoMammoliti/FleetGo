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

    public boolean eliminaVeicolo(String targaVeicolo) {
        String query = "DELETE FROM veicolo WHERE targa = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, targaVeicolo);
            return ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Veicolo> getVeicoliDisponibiliInPiattaforma() {
        String query = "SELECT v.*,a.nome_azienda FROM veicolo v LEFT JOIN  gestione_veicolo_azienda g ON v.id_veicolo=g.id_veicolo LEFT JOIN azienda a " +
                " ON a.id_azienda = g.id_azienda";

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
    public Integer cercaIdAziendaPerTarga(String targa) {
        String query= "SELECT g.id_azienda FROM gestione_veicolo_azienda g JOIN veicolo v ON v.id_veicolo=g.id_veicolo AND v.targa=?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setString(1, targa);
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt("id_azienda") : null;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
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
        String nomeAzienda = rs.getString("nome_azienda");
        if(!rs.wasNull()){
            v.setNomeAziendaAffiliata(nomeAzienda);
        }else {
            v.setNomeAziendaAffiliata(null);
        }
        return v;
    }
}
