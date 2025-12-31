package it.unical.fleetgo.backend.Exceptions;

public class ModelloAssegnatoAVeicoli extends IllegalStateException {
    public ModelloAssegnatoAVeicoli() {
        super("Esistono veicoli con questo modello. Non è possibile eliminarlo");
    }
}