package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.FatturaDTO;
import it.unical.fleetgo.backend.Models.Proxy.FatturaProxy;
import it.unical.fleetgo.backend.Persistence.Entity.Fattura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FatturaDAO {

    private final Connection connection;

    public FatturaDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserisciFattura(FatturaDTO fattura){
        String query = "INSERT INTO fattura(id_azienda, fattura_pagata, costo, mese_fattura, anno_fattura, id_offerta_applicata) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fattura.getIdAzienda());
            ps.setBoolean(2, fattura.isFatturaPagata());
            ps.setDouble(3, fattura.getCosto());
            ps.setInt(4, fattura.getMeseFattura());
            ps.setInt(5, fattura.getAnnoFattura());

            if (fattura.getIdOffertaApplicata() != null && fattura.getIdOffertaApplicata() != 0) {
                ps.setInt(6, fattura.getIdOffertaApplicata());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean pagaFattura(FatturaDTO fattura) {
        String query = "UPDATE fattura SET fattura_pagata = true WHERE id_fleetgo = ? AND id_azienda = ? AND mese_fattura = ? AND anno_fattura = ?";
        return getFattura(fattura, query);
    }

    public List<Fattura> getFattureEmesseDaFleetGo(Integer anno) {
        String query = "SELECT * FROM fattura WHERE anno_fattura = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, anno);
            return creaListaFattura(ps);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Fattura getFatturaByNumeroFattura(Integer numeroFattura) {
        String query = "SELECT * from fattura WHERE numero_fattura = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, numeroFattura);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Fattura f = new FatturaProxy(new AziendaDAO(connection), new OffertaDAO(connection));
                f.setIdAzienda(rs.getInt("id_azienda"));
                f.setFatturaPagata(rs.getBoolean("fattura_pagata"));
                f.setCosto(rs.getFloat("costo"));
                f.setMeseFattura(rs.getInt("mese_fattura"));
                f.setAnnoFattura(rs.getInt("anno_fattura"));
                f.setNumeroFattura(numeroFattura);
                f.setIdOffertaApplicata(rs.getInt("id_offerta_applicata"));
                return f;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getAnniFatture() {
        String query = "SELECT DISTINCT anno_fattura FROM fattura";

        try(PreparedStatement ps = connection.prepareStatement(query)) {

            List<Integer> anni = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                anni.add(rs.getInt("anno_fattura"));
            }

            return anni;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Fattura> getFattureEmesseAdAzienda(Integer idAzienda) {
        String query = "SELECT * FROM fattura WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            return creaListaFattura(ps);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Fattura> creaListaFattura(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<Fattura> fatture = new ArrayList<>();

        while(rs.next()) {
            Fattura f = new FatturaProxy(new AziendaDAO(connection), new OffertaDAO(connection));
            f.setNumeroFattura(rs.getInt("numero_fattura"));
            f.setIdAzienda(rs.getInt("id_azienda"));
            f.setAnnoFattura(rs.getInt("anno_fattura"));
            f.setMeseFattura(rs.getInt("mese_fattura"));
            f.setCosto(rs.getFloat("costo"));
            f.setFatturaPagata(rs.getBoolean("fattura_pagata"));
            f.setIdOffertaApplicata(rs.getInt("id_offerta_applicata"));
            fatture.add(f);
        }
        return fatture;
    }

    private boolean getFattura(FatturaDTO fattura, String query) {
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fattura.getIdAzienda());
            ps.setInt(2, fattura.getMeseFattura());
            ps.setInt(3, fattura.getAnnoFattura());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}