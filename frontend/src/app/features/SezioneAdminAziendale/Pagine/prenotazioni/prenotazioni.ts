import {Component, OnInit} from '@angular/core';
import {
  ModaleDettagliPrenotazione
} from '@features/SezioneAdminAziendale/Componenti/modali/modale-dettagli-prenotazione/modale-dettagli-prenotazione';
import {
  TabellaPrenotazioni
} from '@features/SezioneAdminAziendale/Componenti/tabelle/tabella-prenotazioni/tabella-prenotazioni';
import {RichiestaNoleggioDTO} from '@core/models/RichiestaNoleggioDTO';
import {PrenotazioniService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/prenotazioni-service';
import {
  BannerNoleggiDaApprovare
} from '@features/SezioneAdminAziendale/Componenti/banner/banner-noleggi-da-approvare/banner-noleggi-da-approvare';
import {FormsModule} from '@angular/forms';
import {
  ModaleApprovazioneNoleggi
} from '@features/SezioneAdminAziendale/Componenti/modali/modale-approvazione-noleggi/modale-approvazione-noleggi';
import {RisoluzioneConfilittiNoleggio} from '@core/models/RisoluzioneConfilittiNoleggio';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {SceltaTendina} from '@shared/Componenti/Input/scelta-tendina/scelta-tendina';
import {TemplateFinestraModale} from '@shared/Componenti/Modali/template-finestra-modale/template-finestra-modale';
import {BannerErrore} from "@shared/Componenti/Banner/banner-errore/banner-errore";

@Component({
  selector: 'app-prenotazioni',
  imports: [
    ModaleDettagliPrenotazione,
    TabellaPrenotazioni,
    BannerNoleggiDaApprovare,
    FormsModule,
    ModaleApprovazioneNoleggi,
    TemplateTitoloSottotitolo,
    SceltaTendina,
    TemplateFinestraModale,
    BannerErrore
  ],
  templateUrl: './prenotazioni.html',
  styleUrl: './prenotazioni.css',
})

export class Prenotazioni implements OnInit {

  constructor(private prenotazioniService: PrenotazioniService) {}

  richiesteNoleggio: RichiestaNoleggioDTO[] | null = null;
  modaleDettaglioAperto = false;
  dettaglioDellaPrenotazione = {} as RichiestaNoleggioDTO;

  filtroStato = "Tutti";
  filtroDataInizio = "";
  filtroDataFine = "";

  erroreBanner = "";
  successoBanner = "";

  statiNoleggio = [
    {etichetta: 'Tutti gli stati', id: 'Tutti'},
    {etichetta: 'In corso', id: 'In corso'},
    {etichetta: 'Terminate', id: 'Terminate'},
    {etichetta: 'Da ritirare', id: 'Da ritirare'}
  ];

  numeroNoleggiDaApprovare = 0;
  richiesteDaApprovare: RichiestaNoleggioDTO[] = [];
  modaleAccettazioneAperto = false;

  mostraAlertConflitti: boolean = false;
  richiesteDiInteresse: any = null;

  ngOnInit() {
    this.getPrenotazioni()
    this.getNumeroNoleggiDaApprovare();
  }

  getPrenotazioni() {
    this.prenotazioniService.getPrenotazioni().subscribe({
      next: value => {
        if (value) this.richiesteNoleggio = value;
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  getNumeroNoleggiDaApprovare() {
    this.prenotazioniService.getNumeroNoleggiDaApprovare().subscribe({
      next: value => {
        if (value !== undefined && value !== null) this.numeroNoleggiDaApprovare = value;
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  get prenotazioniFiltrate() {
    if (!this.richiesteNoleggio) {
      return null
    }
    return this.richiesteNoleggio.filter(richiestaNoleggio => {

      const stato = richiestaNoleggio.statoRichiesta ? richiestaNoleggio.statoRichiesta?.toLowerCase() : "";
      const filtro = this.filtroStato ? this.filtroStato.toLowerCase() : "";

      let matchStato = true;

      if (filtro && filtro != "tutti") {
        matchStato = false;

        if (filtro === "in corso") {
          matchStato = stato === "in corso";
        } else if (filtro === "da ritirare") {
          matchStato = stato === "da ritirare";
        } else if (filtro === "terminate") {
          matchStato = stato === "terminata";
        }
      }

      const dataNoleggio = richiestaNoleggio.dataRitiro ? richiestaNoleggio.dataRitiro.toString().substring(0, 7) : '';

      let matchData = true;
      if (this.filtroDataInizio) matchData = matchData && dataNoleggio >= this.filtroDataInizio;
      if (this.filtroDataFine) matchData = matchData && dataNoleggio <= this.filtroDataFine;

      return matchStato && matchData;
    })
  }

  apriModale(idRichiesta: number) {
    this.prenotazioniService.getPrenotazioneDettagliata(idRichiesta).subscribe({
      next: value => {
        if (value) this.dettaglioDellaPrenotazione = value;
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
    this.modaleDettaglioAperto = true;
  }

  chiudiModale() {
    this.dettaglioDellaPrenotazione = {} as RichiestaNoleggioDTO;
    this.modaleDettaglioAperto = false;
  }

  resettaFiltri() {
    this.filtroStato = "";
    this.filtroDataInizio = "";
    this.filtroDataFine = "";
  }

  gestisciVisibilitaModaleAccettazioneNoleggi(senzaSwitch: boolean = false) {
    if (!senzaSwitch) {
      this.modaleAccettazioneAperto = !this.modaleAccettazioneAperto;
    }

    if (this.modaleAccettazioneAperto) {
      this.prenotazioniService.getPrenotazioniDaAccettare().subscribe({
        next: value => {
          if (value) this.richiesteDaApprovare = value;
        }, error: err => {
          this.gestisciErrore(err.error);
        }
      })
    } else {
      this.richiesteDaApprovare = [];
    }
  }

  approvaRichiesta(idRichiesta: number) {
    this.prenotazioniService.approvaRichiesta(idRichiesta).subscribe({
      next: value => {
        if (value) {
          this.getPrenotazioni();

          this.prenotazioniService.getNumeroNoleggiDaApprovare().subscribe(numero => {
            this.numeroNoleggiDaApprovare = numero;

            if (this.numeroNoleggiDaApprovare === 0) {
              this.modaleAccettazioneAperto = false;
              this.chiudiModale();
            } else {
              this.gestisciVisibilitaModaleAccettazioneNoleggi(true);
              this.chiudiModale();
            }
          });

          this.resettaFiltri();
          this.gestisciSuccesso('Prenotazione accettata con successo!');
        }
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  rifiutaRichiesta(idRichiesta: number) {
    this.prenotazioniService.rifiutaRichiesta(idRichiesta).subscribe({
      next: value => {
        if (value) {
          this.getPrenotazioni();

          this.prenotazioniService.getNumeroNoleggiDaApprovare().subscribe(numero => {
            this.numeroNoleggiDaApprovare = numero;

            if (this.numeroNoleggiDaApprovare === 0) {
              this.modaleAccettazioneAperto = false;
              this.chiudiModale();
            } else {
              this.gestisciVisibilitaModaleAccettazioneNoleggi(true);
              this.chiudiModale();
            }
          });

          this.resettaFiltri();
          this.gestisciSuccesso('Prenotazione rifiutata con successo!');
        }
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }


  apriModaleCheck(richieste: RisoluzioneConfilittiNoleggio) {
    this.richiesteDiInteresse = richieste;
    this.mostraAlertConflitti = true;
  }


  accettazioneConRifiuto() {
    if (!this.richiesteDiInteresse) return;

    this.prenotazioniService.rifiutoAutomaticoRichieste(this.richiesteDiInteresse).subscribe({
      next: value => {
        if (value) {
          this.mostraAlertConflitti = false;
          this.getPrenotazioni();


          this.prenotazioniService.getNumeroNoleggiDaApprovare().subscribe(numero => {
            this.numeroNoleggiDaApprovare = numero;

            if (this.numeroNoleggiDaApprovare === 0) {
              this.modaleAccettazioneAperto = false;
            } else {
              this.gestisciVisibilitaModaleAccettazioneNoleggi(true);
            }
          });

          this.resettaFiltri();
        }
        this.gestisciSuccesso('Prenotazione accettata con successo!');
      },
      error: err => {
        this.gestisciErrore(err.error);
      }
    });
  }

  gestisciErrore(messaggio: string) {
    this.successoBanner = '';
    this.erroreBanner = messaggio;
    setTimeout(() => this.erroreBanner = '', 5000);
  }

  gestisciSuccesso(messaggio: string) {
    this.erroreBanner = '';
    this.successoBanner = messaggio;
    setTimeout(() => this.successoBanner = '', 3000);
  }
}
