package it.unical.fleetgo.backend.Exceptions;

import java.sql.SQLException;

public class EmailEsistente extends RuntimeException {
    public EmailEsistente() {
        super("Email già esistente. Riprovare con un nuovo indirizzo");
    }
}