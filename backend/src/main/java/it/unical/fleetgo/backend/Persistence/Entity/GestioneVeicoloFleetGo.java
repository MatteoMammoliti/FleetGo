package it.unical.fleetgo.backend.Persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GestioneVeicoloFleetGo {
    private Utente fleetgo;
    private Veicolo veicolo;
    private Azienda azienda;
    private boolean noleggiata;

}
