package it.unical.fleetgo.backend.Persistence.DAO;
import it.unical.fleetgo.backend.Exceptions.TargaPresente;
import it.unical.fleetgo.backend.Exceptions.VeicoloAssegnato;
import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.Entity.LuogoAzienda;
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

    public boolean aggiungiVeicolo(VeicoloDTO veicoloDTO) {
        String query = "INSERT INTO veicolo(targa, modello_veicolo, tipo_distribuzione_veicolo) VALUES (?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query))  {
            ps.setString(1, veicoloDTO.getTargaVeicolo());
            ps.setInt(2, veicoloDTO.getIdModello());
            ps.setString(3, veicoloDTO.getTipoDistribuzioneVeicolo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {

            if(e.getSQLState().equals("23505")) {
                throw new TargaPresente();
            }

            throw new RuntimeException(e);
        }
    }

    public boolean eliminaVeicolo(String targaVeicolo) {

        String controlloAssegmanento = "SELECT 1 FROM veicolo v JOIN gestione_veicolo_azienda g ON v.id_veicolo = g.id_veicolo WHERE v.targa = ?";

        try(PreparedStatement ps = connection.prepareStatement(controlloAssegmanento)) {
            ps.setString(1, targaVeicolo);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                throw new VeicoloAssegnato("Veicolo assegnato ad un azienda. Revocarlo prima di eliminarlo");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "DELETE FROM veicolo WHERE targa = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, targaVeicolo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Veicolo> getVeicoliDisponibiliInPiattaforma() {
        String query = "SELECT v.*,a.nome_azienda,a.id_azienda, m.url_immagine, m.nome_modello FROM veicolo v LEFT JOIN " +
                " gestione_veicolo_azienda g ON v.id_veicolo=g.id_veicolo LEFT JOIN azienda a " +
                " ON a.id_azienda = g.id_azienda JOIN modelli_veicolo m ON v.modello_veicolo = m.id_modello";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            List<Veicolo> veicoli = new ArrayList<>();

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                veicoli.add(getVeicoloDaResultSet(rs,false,true));
            }
            return veicoli;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Veicolo> getVeicoliAssegnatiAzienda(Integer idAzienda) {
        String query = "SELECT v.*,a.nome_azienda,a.id_azienda, m.url_immagine, m.nome_modello, l.* FROM veicolo v " +
                " LEFT JOIN  gestione_veicolo_azienda g ON v.id_veicolo=g.id_veicolo LEFT JOIN azienda a " +
                " ON a.id_azienda = g.id_azienda JOIN modelli_veicolo m ON v.modello_veicolo = m.id_modello " +
                " LEFT JOIN luogo_azienda l ON l.id_luogo = g.luogo_ritiro_consegna WHERE a.id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            List<Veicolo> veicoli = new ArrayList<>();

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                veicoli.add(getVeicoloDaResultSet(rs,true,true));
            }
            return veicoli;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Veicolo getVeicoloDaId(Integer idVeicolo) {
        String query = "SELECT v.*, m.url_immagine, m.nome_modello FROM veicolo v JOIN modelli_veicolo m ON v.modello_veicolo = m.id_modello WHERE id_veicolo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idVeicolo);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return getVeicoloDaResultSet(rs,false,false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Veicolo getVeicoloDaIdConLuogo(Integer idVeicolo) {
        String query = "SELECT v.*, l.*, a.nome_azienda, a.id_azienda, m.url_immagine, m.nome_modello " +
                "FROM veicolo v " +
                "LEFT JOIN gestione_veicolo_azienda g ON v.id_veicolo = g.id_veicolo " +
                "LEFT JOIN luogo_azienda l ON g.luogo_ritiro_consegna = l.id_luogo " +
                "LEFT JOIN azienda a ON g.id_azienda = a.id_azienda " +
                "JOIN modelli_veicolo m ON v.modello_veicolo = m.id_modello " +
                "WHERE v.id_veicolo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idVeicolo);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return getVeicoloDaResultSet(rs, true, false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Veicolo getVeicoloByTarga(String targa) {
        String query = "SELECT v.*,l.*,a.nome_azienda,a.id_azienda, m.url_immagine, m.nome_modello " +
                "FROM veicolo v LEFT JOIN gestione_veicolo_azienda g ON v.id_veicolo = g.id_veicolo " +
                "LEFT JOIN luogo_azienda l ON g.luogo_ritiro_consegna = l.id_luogo " +
                "LEFT JOIN azienda a ON g.id_azienda = a.id_azienda " +
                "JOIN modelli_veicolo m ON v.modello_veicolo = m.id_modello " +
                "WHERE v.targa = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, targa);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return getVeicoloDaResultSet(rs,true,false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean cambiaStatusContrattualeVeicolo(String nuovoStatus, Integer idVeicolo) {
        String query = "UPDATE veicolo SET status_contrattuale = ? WHERE id_veicolo = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nuovoStatus);
            ps.setInt(2, idVeicolo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Veicolo getVeicoloDaResultSet(ResultSet rs,boolean conLuogo,boolean soloAzienda) throws SQLException {
        Veicolo v = new Veicolo();
        v.setIdVeicolo(rs.getInt("id_veicolo"));
        v.setTargaVeicolo(rs.getString("targa"));
        v.setNomeModello(rs.getString("nome_modello"));
        v.setIdModello(rs.getInt("modello_veicolo"));
        v.setUrlImmagine(rs.getString("url_immagine"));
        v.setTipoDistribuzioneVeicolo(rs.getString("tipo_distribuzione_veicolo"));
        v.setStatusContrattualeVeicolo(rs.getString("status_contrattuale"));
        v.setInManutenzione(rs.getBoolean("in_manutenzione"));
        if(conLuogo){
            LuogoAzienda luogo = new LuogoAzienda();
            luogo.setIdLuogo(rs.getInt("id_luogo"));
            luogo.setIdAzienda(rs.getInt("id_azienda"));
            luogo.setNomeLuogo(rs.getString("nome_luogo"));
            luogo.setLatitudine(rs.getFloat("latitudine"));
            luogo.setLongitudine(rs.getFloat("longitudine"));
            v.setNomeAziendaAffiliata(rs.getString("nome_azienda"));;
            v.setIdAziendaAffiliata(rs.getInt("id_azienda"));
            v.setLuogo(luogo);
        }
        if(soloAzienda){
            v.setNomeAziendaAffiliata(rs.getString("nome_azienda"));
            v.setIdAziendaAffiliata(rs.getInt("id_azienda"));
        }
        return v;
    }






}