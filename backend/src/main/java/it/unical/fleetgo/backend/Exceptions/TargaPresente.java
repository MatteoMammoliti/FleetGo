package it.unical.fleetgo.backend.Exceptions;

public class TargaPresente extends RuntimeException {
    public TargaPresente() {
        super("Targa già esistente. Riprovare con un nuovo numero di targa");
    }
}