package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichiestaNoleggioDTO {
    private Integer idRichiesta;
    private Integer idDipendente;
    private Integer idAziendaRiferimento;
    private Integer idVeicolo;
    private String oraInizio;
    private String oraFine;
    private String dataRitiro;
    private String dataConsegna;
    private String motivazione;
    private boolean accettata;
}
