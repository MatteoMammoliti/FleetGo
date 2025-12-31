package it.unical.fleetgo.backend.Exceptions;

public class FattureDaGenerare extends IllegalStateException {
    public FattureDaGenerare() {
        super("Non è stato possibile disabilitare l'azienda poichè esistono fatture da generare");
    }
}