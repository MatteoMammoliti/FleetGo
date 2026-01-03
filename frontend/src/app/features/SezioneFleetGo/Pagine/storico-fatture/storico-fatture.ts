import { Component } from '@angular/core';
import {StoricoFattureService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/storico-fatture-service';
import {AziendeAffiliateService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/aziende-affiliate-service';
import {FatturaDTO} from '@core/models/FatturaDTO.models';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {CardStatisticheDashboardFleet} from '@shared/Componenti/Ui/card-statistiche-dashboard-fleet/card-statistiche-dashboard-fleet';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {
  TabellaStoricoFatture
} from '@features/SezioneFleetGo/Componenti/tabelle/tabella-storico-fatture/tabella-storico-fatture';
import {IntestazioneEBackground} from '@shared/Componenti/Ui/intestazione-ebackground/intestazione-ebackground';
import {BannerErrore} from "@shared/Componenti/Ui/banner-errore/banner-errore";

@Component({
  selector: 'app-storico-fatture',
    imports: [
        CardStatisticheDashboardFleet,
        ReactiveFormsModule,
        FormsModule,
        TabellaStoricoFatture,
        IntestazioneEBackground,
        BannerErrore
    ],
  templateUrl: './storico-fatture.html',
  styleUrl: './storico-fatture.css',
})
export class StoricoFatture {

  constructor(private storicoFattureService:StoricoFattureService,
              private aziendeAffiliateService: AziendeAffiliateService) {}

  fatturePerAnno: FatturaDTO[] | null = null;
  listaAziende: AziendaDTO[] = [];
  anniFatture: number[] = [];

  protected annoSelezionato: number = new Date().getFullYear();
  protected aziendaSelezionata: any = "";

  totaleFattureAnnoSelezionato = 0;
  totaleRicaviAnno = 0;
  totaleRicaviAnnoStimato = 0;
  erroreBanner="";
  successoBanner="";

  ngOnInit() {
    this.prelevaAnni();
    this.prelevaAziende();
    this.aggiornaFatture();
  }


  private prelevaAnni(){
    this.storicoFattureService.getAnniFatture().subscribe({
      next: (anni) => {
        this.anniFatture = anni;

        if(anni.length > 0 && !anni.includes(this.annoSelezionato)) {
          this.annoSelezionato = anni[anni.length - 1];
          this.aggiornaFatture();
        }
      },
      error: (err) => {
        this.gestisciErrore(err.error);

      }
    })

  }
  private prelevaAziende(){
    this.aziendeAffiliateService.richiediAziendeAttive().subscribe({
      next: (aziende) => {
        this.listaAziende = aziende;
      },
      error: (err) => {
        this.gestisciErrore(err.error);

      }
    })
  }


  protected aggiorna(dati:any){
    this.annoSelezionato=dati.annoSelezionato;
    this.aziendaSelezionata=dati.aziendaSelezionata;
    this.aggiornaFatture();
  }

  protected aggiornaFatture() {

    if(!this.annoSelezionato) return;

    this.storicoFattureService.getFatturePerAnno(this.annoSelezionato).subscribe({
      next: (fatturePerAnno: FatturaDTO[]) => {
        this.fatturePerAnno = fatturePerAnno;
        this.calcolaDati();
      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    })
  }

  private calcolaDati() {
    if(!this.fatturePerAnno) return;

    this.totaleFattureAnnoSelezionato = this.fatturePerAnno.length;

    this.totaleRicaviAnno = 0;
    this.totaleRicaviAnnoStimato = 0;
    for(const fattura of this.fatturePerAnno) {
      if(fattura.fatturaPagata) this.totaleRicaviAnno += fattura.costo;
    }

    this.totaleRicaviAnnoStimato = this.totaleRicaviAnno;
    for(const fattura of this.fatturePerAnno) {
      if(!fattura.fatturaPagata) this.totaleRicaviAnnoStimato += fattura.costo;
    }
  }

  get FattureFiltrate(): FatturaDTO[]|null {
    if(!this.fatturePerAnno) return null;
    if(!this.aziendaSelezionata) return this.fatturePerAnno;

    return this.fatturePerAnno.filter(
      f => f.azienda.nomeAzienda === this.aziendaSelezionata
    )
  }

  protected gestisciDownload(idFattura: number) {
    this.storicoFattureService.downloadFattura(idFattura).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const pdf = document.createElement('a');
      pdf.href = url;
      pdf.download = 'Fattura.pdf';
      pdf.click();
      window.URL.revokeObjectURL(url);
    });
  }
  gestisciErrore(messaggio: string) {
    this.successoBanner = '';
    this.erroreBanner = messaggio;
    setTimeout(() => this.erroreBanner = '', 5000);
  }


}
