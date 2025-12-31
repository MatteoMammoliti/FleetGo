package it.unical.fleetgo.backend.Exceptions;

public class DatePrenotazioneNonValide extends RuntimeException {
    public DatePrenotazioneNonValide() {
        super("Le date inserite non sono valide (fine prima dell'inizio)");
    }
}