package it.unical.fleetgo.backend.Exceptions;

public class NoleggiInCorsoPresenti extends IllegalStateException {
    public NoleggiInCorsoPresenti() {
        super("Non è stato possibile disabilitare l'azienda poichè esistono richieste di noleggio in corso o accettate");
    }
}