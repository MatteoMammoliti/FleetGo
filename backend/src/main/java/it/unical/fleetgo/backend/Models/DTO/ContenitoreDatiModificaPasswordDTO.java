package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContenitoreDatiModificaPasswordDTO {
    private String email;
    private String nuovaPassword;
    private String codiceOTP;
}