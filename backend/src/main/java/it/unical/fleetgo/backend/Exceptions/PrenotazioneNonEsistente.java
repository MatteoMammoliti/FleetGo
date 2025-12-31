package it.unical.fleetgo.backend.Exceptions;

public class PrenotazioneNonEsistente extends RuntimeException {
    public PrenotazioneNonEsistente() {
        super("Impossibile eliminare: Nessuna prenotazione trovata");
    }
}