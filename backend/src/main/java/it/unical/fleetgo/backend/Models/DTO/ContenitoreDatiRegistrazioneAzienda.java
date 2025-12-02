package it.unical.fleetgo.backend.Models.DTO;

import it.unical.fleetgo.backend.Models.DTO.Utente.AdminAziendaleDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContenitoreDatiRegistrazioneAzienda {
    public AziendaDTO azienda;
    public AdminAziendaleDTO adminAziendale;
}