package it.unical.fleetgo.backend.Persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GestioneVeicoloFleetGo {
    private Integer fleetgo;
    private Integer idVeicolo;
    private Veicolo veicolo;
    private Integer idAzienda;
    private boolean noleggiata;

}
