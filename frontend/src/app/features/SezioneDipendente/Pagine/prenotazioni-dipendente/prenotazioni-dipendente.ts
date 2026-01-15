import {Component, OnInit} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/RichiestaNoleggioDTO';
import {PrenotazioneCard} from '@features/SezioneDipendente/componenti/Card/prenotazione-card/prenotazione-card';
import {PrenotazioniService} from '@features/SezioneDipendente/ServiceSezioneDipendente/prenotazioni-service';
import {Router} from '@angular/router';
import {MessaggioCardVuota} from '@shared/Componenti/Banner/messaggio-card-vuota/messaggio-card-vuota';
import {CommonModule} from '@angular/common';
import {BottonePillola} from '@shared/Componenti/Bottoni/bottone-pillola/bottone-pillola';
import {BannerErrore} from '@shared/Componenti/Banner/banner-errore/banner-errore';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {TemplateFinestraModale} from '@shared/Componenti/Modali/template-finestra-modale/template-finestra-modale';

@Component({
  selector: 'app-prenotazioni-dipendente',
  imports: [
    PrenotazioneCard,
    TemplateTitoloSottotitolo,
    MessaggioCardVuota
    , CommonModule,
    BottonePillola,
    BannerErrore, TemplateFinestraModale
  ],
  templateUrl: './prenotazioni-dipendente.html',
  styleUrl: './prenotazioni-dipendente.css',
})

export class PrenotazioniDipendente implements OnInit {
  constructor(private service: PrenotazioniService,
              private router: Router) {
  }

  prenotazioni: RichiestaNoleggioDTO[] = [];

  filtroAttivo: string = "Tutte"

  successoBanner: string = '';
  erroreBanner: string = '';

  caricamentoDati: boolean = true;

  modaleAnnullaPrenotazione: boolean = false;
  richiestaInterssata:any=null;

  ngOnInit() {
    this.getRichiesteDipendente();
  }

  getRichiesteDipendente() {
    this.caricamentoDati = true;
    this.service.richiediPrenotazioniDipendente().subscribe({
      next: (risposta: RichiestaNoleggioDTO[]) => {
        this.prenotazioni = risposta
        this.caricamentoDati = false;
      },
      error: (err) => {
        this.gestisciErrore(err.error);
        this.caricamentoDati = false;
      }
    });
  }

  eliminaPrenotazione() {
    this.service.eliminaPrenotazione(this.richiestaInterssata).subscribe({
      next: (risposta: any) => {
        this.getRichiesteDipendente()
        this.gestisciSuccesso("Prenotazione annullata con successo!");
        this.chiudiModaleCheck();
      },
      error: (err) => {
        this.gestisciErrore(err.error);
        this.chiudiModaleCheck();
      }
    })
  }


  clickNuovaPrenotazione() {
    this.router.navigate(['/dashboardDipendente/nuovaPrenotazione'])
  }

  get prenotazioniFiltrate() {
    if (this.filtroAttivo === 'Tutte') {
      return this.prenotazioni;
    }

    return this.prenotazioni.filter(prenotazione => {

      let stato: boolean | undefined = false;

      if (this.filtroAttivo === 'Rifiutate') {
        stato = prenotazione.richiestaAnnullata;
      } else {
        stato = prenotazione.statoRichiesta == this.filtroAttivo && !prenotazione.richiestaAnnullata;
      }

      return stato;
    });
  }

  impostaFiltro(categoria: string) {
    if (this.filtroAttivo === categoria) return;

    this.caricamentoDati = true;
    this.filtroAttivo = categoria;

    setTimeout(() => {
      this.caricamentoDati = false;
    }, 0);
  }

  apriModaleCheck(idPrenotazione: number) {
    this.richiestaInterssata=idPrenotazione;
    this.modaleAnnullaPrenotazione=true;
  }

  chiudiModaleCheck(){
    this.modaleAnnullaPrenotazione=false;
    this.richiestaInterssata=null;
  }

  confermaModaleCheck(){
    this.eliminaPrenotazione();
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
