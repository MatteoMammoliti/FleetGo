package it.unical.fleetgo.backend.Exceptions;

public class ManutenzioneEsistente extends RuntimeException {
    public ManutenzioneEsistente(String message) {
        super(message);
    }
}