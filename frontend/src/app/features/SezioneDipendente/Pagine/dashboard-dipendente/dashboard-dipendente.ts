import {Component, OnInit} from '@angular/core';
import {BannerHome} from '@features/SezioneDipendente/componenti/Banner/banner-home/banner-home';
import {ProssimoViaggio} from '@features/SezioneDipendente/componenti/Banner/prossimo-viaggio/prossimo-viaggio';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {HomeService} from '@features/SezioneDipendente/ServiceSezioneDipendente/home-service';
import {StatisticheDipendenteDTO} from '@core/models/StatisticheDipendenteDTO';
import {StatisticheDipendente} from '@features/SezioneDipendente/componenti/Banner/statistiche-dipendente/statistiche-dipendente';
import {Router} from '@angular/router';
import {MappaHub} from '@features/SezioneDipendente/componenti/Banner/mappa-hub/mappa-hub';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {SupportoFleetgo} from '@features/SezioneDipendente/componenti/Banner/supporto-fleetgo/supporto-fleetgo';
import {DatePipe} from '@angular/common';
import {ModaleRichiestaAssistenza} from '@features/SezioneDipendente/componenti/Modali/modale-richiesta-assistenza/modale-richiesta-assistenza';
import { BannerErrore } from '@shared/Componenti/Banner/banner-errore/banner-errore';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';

@Component({
  selector: 'app-dashboard-dipendente',
  imports: [
    BannerHome,
    ProssimoViaggio,
    StatisticheDipendente,
    MappaHub,
    SupportoFleetgo,
    ModaleRichiestaAssistenza,
    TemplateTitoloSottotitolo,
    BannerErrore
  ],
  providers: [DatePipe],
  templateUrl: './dashboard-dipendente.html',
  styleUrl: './dashboard-dipendente.css',
})

export class DashboardDipendente implements OnInit {
  constructor(private service:HomeService,
              private router:Router,
              private datePipe:DatePipe) {}

  prossimoViaggio:RichiestaNoleggioDTO | undefined;
  statisticheDipendente:StatisticheDipendenteDTO | undefined;
  luoghiAzienda:LuogoDTO[]=[]
  nomeDipendente=""

  listaOggettiDinamica: any[] = [];
  modaleContattoVisibile = false;
  caricamentoDati = false;
  categorieContatto = ["Veicoli/Guasti", "Noleggi in corso", "Noleggi passati"]
  listaOggettiDinamicaVisibile = false;

  erroreBanner="";
  successoBanner="";

  ngOnInit(){
    this.richiediNomeDipendente();
    this.richiediProssimoViaggio();
    this.richiediStatisticheDipendente();
    this.richiediLuoghiAzienda();
  }

  richiediNomeDipendente(){
    this.service.richiediNomeDipendente().subscribe({
      next:(risposta:string)=>{
        this.nomeDipendente=risposta;
      },
      error:(err)=>this.gestisciErrore(err.error),
    });
  }

  richiediProssimoViaggio(){
    this.service.richiediProssimoViaggio().subscribe({
      next:(risposta:RichiestaNoleggioDTO)=>{
        this.prossimoViaggio=risposta
      },
        error:(err)=>{this.gestisciErrore(err.error);
        }
      })
    }

  richiediStatisticheDipendente(){
    this.service.richiediStatisticheDipendente().subscribe({
      'next':(risposta:StatisticheDipendenteDTO)=>{
        this.statisticheDipendente=risposta;
      },
      'error':(err)=>{this.gestisciErrore(err.error)}
    })
  }

  richiediLuoghiAzienda(){
    this.service.richiediLuoghiAzienda().subscribe({
      next:(risposta:LuogoDTO[])=>{
        this.luoghiAzienda=risposta;
      },
      error:(err)=>this.gestisciErrore(err.error)
    })
  }

  apriPrenotazioni(){
    this.router.navigate(['/dashboardDipendente/nuovaPrenotazione']);
  }
  apriMieAttivita(){
    this.router.navigate(['/dashboardDipendente/prenotazioni'])
  }

  gestisciVisibilita() {
    this.modaleContattoVisibile = !this.modaleContattoVisibile;
    this.listaOggettiDinamica=[];
    this.listaOggettiDinamicaVisibile=false;
  }

  caricaDatiContesto(categoria: string) {

    this.caricamentoDati = true;
    this.listaOggettiDinamicaVisibile = false;

    if (categoria.includes('Noleggi')) {

      this.listaOggettiDinamicaVisibile = true;

      this.service.richiediPrenotazioni().subscribe({
        next: (prenotazioni) => {
          let filtrate = [];

          if (categoria === 'Noleggi in corso') {
            filtrate = prenotazioni.filter(p => p.statoRichiesta === 'In corso' || p.statoRichiesta === 'Accettata');
          } else {
            filtrate = prenotazioni.filter(p => p.statoRichiesta === 'Terminata');
          }

          if (filtrate.length === 0) {
            this.listaOggettiDinamica = [{
              labelVisuale: 'Nessun noleggio trovato'
            }];
          } else {
            this.listaOggettiDinamica = filtrate.map(p => ({
              labelVisuale: `Noleggio del ${this.datePipe.transform(p.dataRitiro, 'dd/MM/yyyy')}`
            }));
          }

          this.caricamentoDati = false;
        },
        error: () => {
          this.caricamentoDati = false;
        }
      });
    }
  }

  inviaSegnalazione(messaggioRichiesta: string | null) {

    this.service.inviaSegnalazione(messaggioRichiesta).subscribe({
      next: (inviaSegnalazione) => {
        if(inviaSegnalazione) {
          this.gestisciSuccesso("Segnalazione inviata con successo!");
          this.gestisciVisibilita();
        }
      }, error: err => {
        this.gestisciErrore(err.error);
        this.gestisciVisibilita();
      }
    })
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
