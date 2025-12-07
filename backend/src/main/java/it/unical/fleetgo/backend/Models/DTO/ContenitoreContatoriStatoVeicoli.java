package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContenitoreContatoriStatoVeicoli {
    private int numVeicoliNoleggiati;
    private int numVeicoliInManutenzione;
    private int numVeicoliDisponibili;

    public ContenitoreContatoriStatoVeicoli(int numVeicoliNoleggiati, int numVeicoliInManutenzione, int numVeicoliDisponibili) {
        this.numVeicoliNoleggiati = numVeicoliNoleggiati;
        this.numVeicoliInManutenzione = numVeicoliInManutenzione;
        this.numVeicoliDisponibili = numVeicoliDisponibili;
    }
}