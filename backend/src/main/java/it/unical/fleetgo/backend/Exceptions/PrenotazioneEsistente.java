package it.unical.fleetgo.backend.Exceptions;

public class PrenotazioneEsistente extends RuntimeException {
    public PrenotazioneEsistente() {
        super("Prenotazione esistente per le date selezionate");
    }
}