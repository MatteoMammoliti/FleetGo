package it.unical.fleetgo.backend.Models.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GestioneVeicoloAziendaDTO {
    private Integer idVeicolo;
    private VeicoloDTO Veicolo;
    private Integer idAzienda;
    private Integer idLuogo;
    private LuogoDTO luogo;
}