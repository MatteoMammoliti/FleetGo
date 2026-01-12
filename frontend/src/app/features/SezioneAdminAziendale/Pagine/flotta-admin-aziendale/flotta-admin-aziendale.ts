import {Component, OnInit, ViewChild} from '@angular/core';
import {CardAutoAziendale} from '@shared/Componenti/Card/card-auto-aziendale/card-auto-aziendale';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {FlottaAdminAziendaleService} from '../../ServiceSezioneAdminAziendale/flotta-aziendale-service';
import {ModaleGestisciVeicolo} from '@features/SezioneAdminAziendale/Componenti/modali/modale-gestisci-veicolo/modale-gestisci-veicolo';
import {DettagliVeicoloAziendaleService} from '../../ServiceSezioneAdminAziendale/dettagli-veicolo-aziendale-service';
import {LuogoDTO} from '@core/models/LuogoDTO';
import {SceltaTendina} from '@shared/Componenti/Input/scelta-tendina/scelta-tendina';
import {InputChecked} from '@shared/Componenti/Input/input-checked/input-checked';
import {ActivatedRoute, Router} from '@angular/router';
import {BannerErrore} from '@shared/Componenti/Banner/banner-errore/banner-errore';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {MessaggioCardVuota} from '@shared/Componenti/Banner/messaggio-card-vuota/messaggio-card-vuota';

@Component({
  selector: 'app-flotta-admin-aziendale',
  standalone: true,
  imports: [
    CardAutoAziendale,
    CommonModule,
    FormsModule,
    ModaleGestisciVeicolo,
    SceltaTendina,
    InputChecked,
    BannerErrore,
    TemplateTitoloSottotitolo,
    MessaggioCardVuota
  ],

  templateUrl: './flotta-admin-aziendale.html',
  styleUrl: './flotta-admin-aziendale.css',
})

export class FlottaAdminAziendale implements OnInit {

  constructor(private flottaService: FlottaAdminAziendaleService,
              private dettagliService: DettagliVeicoloAziendaleService,
              private route: ActivatedRoute) {}

  @ViewChild('mioModale') modale!: ModaleGestisciVeicolo;


  veicoli: any[] | null = null;

  testoRicerca: string = '';
  filtroStato: any = null;
  mostraModale = false;
  veicoloSelezionato: any = null;
  listaLuoghi: any[] = [];

  loading: boolean = false;

  erroreBanner = "";
  successoBanner = "";


  ngOnInit(): void {
    this.caricaVeicoli();
    this.caricaLuoghi();
    this.route.queryParams.subscribe(params => {
      if (params['stato']) {
        this.filtroStato = params['stato'].toUpperCase();

      }
    });
  }

  caricaLuoghi() {
    this.flottaService.richiediLuoghi().subscribe({
      next: (data) => {
        this.listaLuoghi = data;
      },
      error: (err) => {
        this.gestisciErrore(err.error())
      }
    });
  }

  caricaVeicoli() {
    this.flottaService.richiediVeicoliAziendali().subscribe({
      next: (data) => {
        if (data) {
          this.veicoli = data;
        }
      },
      error: (err) => {
        this.gestisciErrore(err.error())
      }
    });
  }

  get veicoliFiltrati() {
    if (this.veicoli == null) {
      return
    }
    return this.veicoli.filter(v => {
      const targa = v.targaVeicolo || '';
      const modello = v.nomeModello || '';
      const matchTesto = (targa.toLowerCase().includes(this.testoRicerca.toLowerCase()) ||
        modello.toLowerCase().includes(this.testoRicerca.toLowerCase()));

      let matchStato = true;

      if (this.filtroStato) {
        switch (this.filtroStato.toUpperCase()) {
          case 'DISPONIBILE':
            matchStato = (v.inManutenzione === false) && v.luogoRitiroDeposito?.nomeLuogo != null;
            break;

          case 'MANUTENZIONE':
            matchStato = v.inManutenzione === true;
            break;

          case 'SENZALUOGO':
            matchStato = !v.luogoRitiroDeposito?.nomeLuogo;
            break;

          default:
            matchStato = true;
            break;
        }
      }

      return matchTesto && matchStato;
    });
  }

  getConteggio(stato: string): number {
    if (!this.veicoli || this.veicoli.length === 0) return 0;

    return this.veicoli.filter(v => {
      switch (stato.toUpperCase()) {
        case 'DISPONIBILE':
          return (v.inManutenzione === false) && v.luogoRitiroDeposito?.nomeLuogo != null;

        case 'MANUTENZIONE':
          return v.inManutenzione === true;

        case 'SENZALUOGO':
          return !v.luogoRitiroDeposito?.nomeLuogo;

        default:
          return false;
      }
    }).length;
  }

  apriModaleDettagli(veicoloDallaLista: any) {
    const targa = veicoloDallaLista.targaVeicolo;

    if (targa) {
      this.dettagliService.richiediVeicolo(targa).subscribe({
        next: (veicoloCompleto) => {
          this.veicoloSelezionato = veicoloCompleto;
          this.mostraModale = true;
        },
        error: (err) => {
          this.gestisciErrore(err.error());
          this.veicoloSelezionato = veicoloDallaLista;
          this.mostraModale = true;
        }
      });
    } else {
      this.veicoloSelezionato = veicoloDallaLista;
      this.mostraModale = true;
    }
  }

  chiudiModale() {
    this.mostraModale = false;
    this.veicoloSelezionato = null;
  }

  caricaDettagliVeicolo(targa: string) {
    this.loading = true;
    this.dettagliService.richiediVeicolo(targa).subscribe({
      next: (data) => {
        this.veicoloSelezionato = data;
        this.loading = false;
        if (this.veicoloSelezionato.luogoRitiroDeposito &&
          this.veicoloSelezionato.luogoRitiroDeposito.latitudine &&
          this.veicoloSelezionato.luogoRitiroDeposito.longitudine) {
          this.modale.initMappa();
        }
      },
      error: (err) => {
        this.gestisciErrore(err.error());
        this.loading = false;
      }
    });
  }

  impostaLuogo(luogoScelto: LuogoDTO) {

    if (luogoScelto && this.veicoloSelezionato) {
      const veicoloAggiornato = {
        ...this.veicoloSelezionato,
        luogoRitiroDeposito: luogoScelto,
        inManutenzione: false
      };

      this.dettagliService.aggiornaPosizioneVeicolo(veicoloAggiornato).subscribe({
        next: (res) => {
          this.veicoloSelezionato = veicoloAggiornato;
          if (this.veicoli == null) return;

          const index = this.veicoli.findIndex(v => v.targaVeicolo === veicoloAggiornato.targaVeicolo);

          if (index !== -1) {
            const nuoviVeicoli = [...this.veicoli];
            nuoviVeicoli[index] = veicoloAggiornato;

            this.veicoli = nuoviVeicoli;
          }
          this.gestisciSuccesso("Luogo impostato con successo!");

        },
        error: (err) => {
          this.gestisciErrore(err.error())
        }
      });
    }
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
