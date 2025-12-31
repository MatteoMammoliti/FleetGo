package it.unical.fleetgo.backend.Exceptions;

import java.sql.SQLException;

public class PIVAEsistente extends RuntimeException {
    public PIVAEsistente() {
        super("Partita IVA già esistente. Riprovare con un nuovo indirizzo");
    }
}