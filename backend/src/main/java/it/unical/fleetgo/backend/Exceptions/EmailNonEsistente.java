package it.unical.fleetgo.backend.Exceptions;

public class EmailNonEsistente extends RuntimeException {
    public EmailNonEsistente() {
        super("Email inesistente. Impossibile inviare codice OTP");
    }
}