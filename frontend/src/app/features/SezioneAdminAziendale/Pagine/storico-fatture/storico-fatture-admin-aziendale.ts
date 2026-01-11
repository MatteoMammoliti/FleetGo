import {Component, OnInit} from '@angular/core';
import {
  TabellaStoricoFatture
} from '@features/SezioneAdminAziendale/Componenti/tabelle/tabella-storico-fatture/tabella-storico-fatture';
import {FatturaDTO} from '@core/models/FatturaDTO';
import {
  StoricoFattureServiceAdminAziendale
} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/storico-fatture-service-admin-aziendale';
import {CurrencyPipe} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {TemplateTitoloSottotitolo} from "@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo";
import {SceltaTendina} from '@shared/Componenti/Input/scelta-tendina/scelta-tendina';
import {BannerErrore} from "@shared/Componenti/Banner/banner-errore/banner-errore";

@Component({
  selector: 'app-storico-fatture-admin-aziendale',
  imports: [
    TabellaStoricoFatture,
    CurrencyPipe,
    FormsModule,
    TemplateTitoloSottotitolo,
    SceltaTendina,
    BannerErrore
  ],
  templateUrl: './storico-fatture-admin-aziendale.html',
  styleUrl: './storico-fatture-admin-aziendale.css',
})
export class StoricoFattureAdminAziendale implements OnInit {

  fattureEmesse: FatturaDTO[] | null = null;
  totaleDaPagare = 0;
  anniDisponibili: any[] = [];

  filtroAnno: any = null;
  filtroStato: any = null;

  placeholder = '';

  erroreBanner = "";
  successoBanner = "";

  constructor(private storicoFattureService: StoricoFattureServiceAdminAziendale,
              private route: ActivatedRoute,
              private router: Router,) {
  }

  ngOnInit() {
    this.getFattureEmesse();
    this.getAnniDisponibili();
    this.verificaEsitoPagamento();
  }

  getFattureEmesse() {
    this.storicoFattureService.getFattureEmesse().subscribe({
      next: data => {
        if (data) {
          this.fattureEmesse = data;

          this.totaleDaPagare = 0;

          for (const fattura of this.fattureEmesse) {
            if (!fattura.fatturaPagata) this.totaleDaPagare += fattura.costo;
          }

        }
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  get fattureFiltrate() {
    if (!this.fattureEmesse) return null;
    return this.fattureEmesse.filter(fattura => {

      const matchAnno = fattura.annoFattura.toString() === this.filtroAnno.toString();

      let matchStato = true;
      if (this.filtroStato === "Da pagare") matchStato = !fattura.fatturaPagata;
      if (this.filtroStato === "Pagate") matchStato = fattura.fatturaPagata;

      return matchAnno && matchStato;
    });
  }

  getAnniDisponibili() {
    this.storicoFattureService.getAnniDisponibili().subscribe({
      next: data => {
        if (data) {
          if (data.length == 0) {
            this.placeholder = new Date().getFullYear().toString();
            return
          }
          this.anniDisponibili = data.sort((a, b) => b - a);

          this.filtroAnno = this.anniDisponibili[0];
        }
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  pagaFattura(numeroFattura: number) {
    this.storicoFattureService.pagaFattura(numeroFattura).subscribe({
      next: data => {
        window.location.href = data;
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  scaricaFattura(numeroFattura: number) {
    this.storicoFattureService.downloadFattura(numeroFattura).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const pdf = document.createElement('a');
      pdf.href = url;
      pdf.download = 'Fattura.pdf';
      pdf.click();
      window.URL.revokeObjectURL(url);
    });
  }

  verificaEsitoPagamento() {
    this.route.queryParams.subscribe(params => {

      if (params["success"] === 'true') {
        const numeroFattura = params["fattura"];
        this.segnalaFatturaPagata(numeroFattura);
        this.getFattureEmesse();
        return;
      }

      this.pulisciUrl();
    })
  }

  segnalaFatturaPagata(numeroFattura: number) {
    this.storicoFattureService.contrassegnaFatturaPagata(numeroFattura).subscribe({
      next: () => {
        this.getFattureEmesse();
        this.pulisciUrl();
        this.gestisciSuccesso('Fattura pagata con successo!');
      }, error: err => {
        this.gestisciErrore(err.error);
        this.pulisciUrl();
      }
    });
  }

  pulisciUrl() {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {},
      replaceUrl: true
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
