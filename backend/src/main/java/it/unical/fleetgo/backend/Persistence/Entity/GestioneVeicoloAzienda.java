package it.unical.fleetgo.backend.Persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GestioneVeicoloAzienda {
    private Integer idVeicolo;
    private Veicolo Veicolo;
    private Integer idAzienda;
    private Integer idLuogo;
    private LuogoAzienda luogo;
    private boolean disponbilePerNoleggio;
}