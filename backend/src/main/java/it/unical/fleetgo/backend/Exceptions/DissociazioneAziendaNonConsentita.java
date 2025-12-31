package it.unical.fleetgo.backend.Exceptions;

public class DissociazioneAziendaNonConsentita extends IllegalStateException {
    public DissociazioneAziendaNonConsentita(String message) {
        super(message);
    }
}