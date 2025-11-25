package it.unical.fleetgo.backend.Persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GestioneVeicoloAzienda {
    private Veicolo veicolo;
    private Azienda azienda;
    private LuogoAzienda luogo;
}
