package it.unical.fleetgo.backend.Exceptions;

public class CodiceOTPErrato extends RuntimeException {
    public CodiceOTPErrato() {
        super("Codice OTP errato o scaduto");
    }
}