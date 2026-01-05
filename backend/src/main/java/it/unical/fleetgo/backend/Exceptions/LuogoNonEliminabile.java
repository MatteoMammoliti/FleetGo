package it.unical.fleetgo.backend.Exceptions;

public class LuogoNonEliminabile extends IllegalStateException {
    public LuogoNonEliminabile(String message) {
        super(message);
    }
}