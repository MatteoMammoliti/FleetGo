package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.FatturaDaGenerareDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GeneraFatturaDAO {
    Connection connection;

    public GeneraFatturaDAO(Connection connection) {
        this.connection=connection;
    }

    public List<FatturaDaGenerareDTO> getFatturaDaGenerare(){
        List<FatturaDaGenerareDTO> fatturaDaGenerare=new ArrayList<>();
        String query="SELECT * FROM view_fatture_da_generare";
        try(PreparedStatement st = connection.prepareStatement(query)){
            ResultSet rs=st.executeQuery();
            while(rs.next()){
                FatturaDaGenerareDTO fatturaDaGenerareDTO = new FatturaDaGenerareDTO(
                        rs.getInt("id_azienda"),
                        rs.getString("nome_azienda"),
                        rs.getString("anno_riferimento"),
                        rs.getString("mese_riferimento"),
                        rs.getInt("numero_noleggi"),
                        rs.getFloat("totale_da_fatturare"));
                fatturaDaGenerare.add(fatturaDaGenerareDTO);
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return fatturaDaGenerare;
    }

    public Float getSpesaMensileAzienda(Integer idAzienda) {
        String query = "SELECT totale_da_fatturare FROM view_fatture_da_generare WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getFloat("totale_da_fatturare");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean ciSonoFattureDaGenerare(Integer idAzienda) {
        String query = "SELECT totale_da_fatturare FROM view_fatture_da_generare WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) return rs.getFloat("totale_da_fatturare") > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}