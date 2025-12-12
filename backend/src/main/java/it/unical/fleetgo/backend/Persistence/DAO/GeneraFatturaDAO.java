package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.FatturaDaGenerareDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GeneraFatturaDAO {
    Connection connection;

    public GeneraFatturaDAO(Connection connection) {
        this.connection=connection;
    }

    public List<FatturaDaGenerareDTO> generaFatturaDaGenerare(){
        List<FatturaDaGenerareDTO> fatturaDaGenerare=new ArrayList<>();
        String query="SELECT * FROM view_fatture_da_generare";
        try(PreparedStatement st = connection.prepareStatement(query)){
            ResultSet rs=st.executeQuery();
            while(rs.next()){
                FatturaDaGenerareDTO fatturaDaGenerareDTO=new FatturaDaGenerareDTO(rs.getInt("id_azienda"),rs.getString("nome_azienda"),
                        rs.getString("anno_riferimento"),rs.getString("mese_riferimento"),rs.getInt("numero_noleggi"),
                        rs.getInt("totale_da_fatturare"));
                fatturaDaGenerare.add(fatturaDaGenerareDTO);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return fatturaDaGenerare;
    }
}
