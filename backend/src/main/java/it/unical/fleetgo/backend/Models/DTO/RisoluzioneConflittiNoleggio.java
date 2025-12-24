package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RisoluzioneConflittiNoleggio {
    private Integer idRichiestaDaApprovare;
    private List<Integer> idRichiesteDaRifiutare;
}