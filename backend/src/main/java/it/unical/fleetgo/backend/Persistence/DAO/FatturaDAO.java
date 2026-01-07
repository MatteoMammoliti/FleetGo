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

    public void inserisciFattura(FatturaDTO fattura) throws SQLException {

        try {
            this.connection.setAutoCommit(false);

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
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }

            String inserimentoInFattura = "UPDATE richiesta_noleggio SET inserito_in_fattura = true WHERE id_azienda = ? AND EXTRACT(year FROM data_consegna) = ? AND EXTRACT(month FROM data_consegna) = ? AND accettata = true";

            try(PreparedStatement ps = connection.prepareStatement(inserimentoInFattura)) {
                ps.setInt(1, fattura.getIdAzienda());
                ps.setInt(2, fattura.getAnnoFattura());
                ps.setInt(3, fattura.getMeseFattura());
                ps.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        } finally {
            try {
                this.connection.setAutoCommit(true);
            } catch (SQLException ignored) {}
        }
    }

    public boolean pagaFattura(Integer numeroFattura) {
        String query = "UPDATE fattura SET fattura_pagata = true WHERE numero_fattura = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, numeroFattura);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Fattura> getFattureEmesseDaFleetGo(Integer anno) {
        String query = "SELECT * FROM fattura WHERE anno_fattura = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, anno);
            return creaListaFattura(ps);
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getAnniFatturePerAzienda(Integer idAzienda) {
        String query = "SELECT DISTINCT anno_fattura FROM fattura WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idAzienda);

            List<Integer> anni = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                anni.add(rs.getInt("anno_fattura"));
            }

            return anni;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Fattura> getFattureEmesseAdAzienda(Integer idAzienda) {
        String query = "SELECT * FROM fattura WHERE id_azienda = ?";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            return creaListaFattura(ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getNmeroFattureDaPagareByIdAzienda(Integer idAzienda) {
        String query = "SELECT COUNT(*) as numero FROM fattura WHERE id_azienda = ? AND fattura_pagata = false";

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAzienda);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getInt("numero");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
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
}