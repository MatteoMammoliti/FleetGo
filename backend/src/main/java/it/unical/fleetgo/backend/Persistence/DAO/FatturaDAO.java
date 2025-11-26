package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.FatturaDTO;
import it.unical.fleetgo.backend.Models.Proxy.FatturaProxy;
import it.unical.fleetgo.backend.Persistence.Entity.Fattura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class FatturaDAO {

    private final Connection connection;

    public FatturaDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean inserisciFattura(FatturaDTO fattura){
        String query = "INSERT INTO fattura(id_fleetgo, id_azienda, fattura_pagata, costo, mese_fattura, anno_fattura) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fattura.getIdFleetGo());
            ps.setInt(2, fattura.getIdAzienda());
            ps.setBoolean(3, fattura.isFatturaPagata());
            ps.setInt(4, fattura.getCosto());
            ps.setInt(5, fattura.getMeseFattura());
            ps.setInt(6, fattura.getAnnoFattura());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminaFattura(FatturaDTO fattura){
        String query = "DELETE FROM fattura WHERE id_fleetgo = ? AND id_azienda = ? AND mese_fattura = ? AND anno_fattura = ?";
        return getFattura(fattura, query);
    }

    public boolean pagaFattura(FatturaDTO fattura) {
        String query = "UPDATE fattura SET fattura_pagata = true WHERE id_fleetgo = ? AND id_azienda = ? AND mese_fattura = ? AND anno_fattura = ?";
        return getFattura(fattura, query);
    }

    public Set<Fattura> getFattureEmesseDaFleetGo() {
        String query = "SELECT * FROM fattura";

        try(PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            Set<Fattura> fatture = new HashSet<>();

            while(rs.next()) {
                Fattura f = new FatturaProxy(new AziendaDAO(connection));
                f.setNumeroFattura(rs.getInt("numero_fattura"));
                f.setIdAzienda(rs.getInt("id_azienda"));
                f.setAnnoFattura(rs.getInt("anno_fattura"));
                f.setMeseFattura(rs.getInt("mese_fattura"));
                f.setFleetGo(rs.getInt("id_fleetgo"));
                f.setCosto(rs.getInt("costo"));
                f.setFatturaPagata(rs.getBoolean("fattura_pagata"));
                fatture.add(f);
            }
            return fatture;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Fattura> getFattureEmesseAdAzienda(Integer idAzienda) {
        String query = "SELECT * FROM fattura WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);

            ResultSet rs = ps.executeQuery();
            Set<Fattura> fatture = new HashSet<>();

            while(rs.next()) {
                Fattura f = new FatturaProxy(new AziendaDAO(connection));
                f.setNumeroFattura(rs.getInt("numero_fattura"));
                f.setIdAzienda(rs.getInt("id_azienda"));
                f.setAnnoFattura(rs.getInt("anno_fattura"));
                f.setMeseFattura(rs.getInt("mese_fattura"));
                f.setFleetGo(rs.getInt("id_fleetgo"));
                f.setCosto(rs.getInt("costo"));
                f.setFatturaPagata(rs.getBoolean("fattura_pagata"));
                fatture.add(f);
            }
            return fatture;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean getFattura(FatturaDTO fattura, String query) {
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fattura.getIdFleetGo());
            ps.setInt(2, fattura.getIdAzienda());
            ps.setInt(3, fattura.getMeseFattura());
            ps.setInt(4, fattura.getAnnoFattura());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}