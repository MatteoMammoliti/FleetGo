package it.unical.fleetgo.backend.Exceptions;

public class RichiestaManutenzioneNonValida extends RuntimeException {
    public RichiestaManutenzioneNonValida() {
        super("Richiesta di manutenzione non valida");
    }
}