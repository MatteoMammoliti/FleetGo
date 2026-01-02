package it.unical.fleetgo.backend.Persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraficoTortaFlotta {
    private Integer inUso;
    private Integer disponibili;
    private Integer inManutenzione;
}
