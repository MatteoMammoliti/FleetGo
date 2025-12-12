package it.unical.fleetgo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContenitoreStatisticheNumeriche {
    private int totaleVeicoli;
    private int veicoliAssegnati;
    private int veicoliDisponibili;
    private int veicoliManutenzione;
    private int veicoliNoleggati;
    private int totaleAziende;
    private int fattureDaGenerare;
    private int guadagnoMensile;

    public ContenitoreStatisticheNumeriche(int totaleVeicoli, int veicoliAssegnati, int veicoliDisponibili, int veicoliManutenzione, int totaleAziende,
                                           int fattureDaGenerare, int guadagnoMensile) {
        this.totaleVeicoli=totaleVeicoli;
        this.veicoliAssegnati=veicoliAssegnati;
        this.veicoliDisponibili=veicoliDisponibili;
        this.veicoliManutenzione=veicoliManutenzione;
        this.totaleAziende=totaleAziende;
        this.fattureDaGenerare=fattureDaGenerare;
        this.guadagnoMensile=guadagnoMensile;
    }

    public ContenitoreStatisticheNumeriche(int veicoliNoleggati, int veicoliDisponibili, int veicoliManutenzione) {
        this.veicoliNoleggati = veicoliNoleggati;
        this.veicoliDisponibili = veicoliDisponibili;
        this.veicoliManutenzione = veicoliManutenzione;
    }
}
