package it.unical.fleetgo.backend.Persistence.DAO;

import it.unical.fleetgo.backend.Models.DTO.Utente.UtenteDTO;

public interface UtenteDAO {
    boolean inserisciUtente(UtenteDTO utente);
    boolean eliminaUtente(Integer idUtente);
    UtenteDTO getUtente(Integer idUtente);

}
