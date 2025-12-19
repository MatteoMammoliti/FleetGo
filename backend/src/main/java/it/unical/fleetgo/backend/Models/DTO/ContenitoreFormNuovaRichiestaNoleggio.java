package it.unical.fleetgo.backend.Models.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContenitoreFormNuovaRichiestaNoleggio {
    private VeicoloPrenotazioneDTO veicolo;
    private String dataInizio;
    private String dataFine;
    private String oraInizio;
    private String oraFine;
    private String motivazione;
}
