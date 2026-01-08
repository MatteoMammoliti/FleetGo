import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IntestazioneEBackground } from '@shared/Componenti/Ui/intestazione-ebackground/intestazione-ebackground';
import { ProfiloPersonale } from '../../componenti/profilo-personale/profilo-personale';
import { PatenteDocumentiComponent } from '../../componenti/patente-documenti/patente-documenti';
import { AffiliazioneAzienda } from '../../componenti/affiliazione-azienda/affiliazione-azienda';
import { ImpostazioniService } from '../../ServiceSezioneDipendente/impostazioni-service';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';
import {AuthService} from '@core/auth/auth-service';
import {Router} from '@angular/router';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';

@Component({
  selector: 'app-impostazioni-dipendente',
  standalone: true,
  imports: [
    CommonModule,
    TemplateTitoloSottotitolo,
    ProfiloPersonale,
    PatenteDocumentiComponent,
    AffiliazioneAzienda,
  ],
  templateUrl: './impostazioni-dipendente.html',
  styleUrl: './impostazioni-dipendente.css',
})

export class ImpostazioniDipendenteComponent implements OnInit {

  tabSelezionata: string = 'Profilo';
  sezione: string = 'Profilo';
  utente: ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO;
  urlPatente : string = '';

  erroreBanner="";
  successoBanner="";

  constructor(private impostazioniService: ImpostazioniService,private authService:AuthService,private router:Router) {}

  ngOnInit() {
    this.caricaDatiUtente();
  }

  caricaDatiUtente() {
    this.impostazioniService.getDipendente().subscribe({
      next: (datiServer) => {
        if(datiServer){
          this.utente = datiServer;
        }
      },
      error: (err) => this.gestisciErrore(err.error),
    });

    this.impostazioniService.getUrlPatente().subscribe({
      next: (urlPatenteServer) => {
        this.urlPatente = urlPatenteServer;
      }, error: err => this.gestisciErrore(err.error)
    });
  }

  cambiaTab(tab: string) {
    this.tabSelezionata = tab;
    this.sezione = tab;
  }

  aggiornaPatente(datiPatente: File) {
    this.impostazioniService.aggiornaPatente(datiPatente).subscribe({
      next: (datiPatenteServer) => {
        if(datiPatenteServer){
          this.caricaDatiUtente();
        }
      }, error: err => this.gestisciErrore(err.error)
    })
  }

  inviaDisdetta(){
    this.impostazioniService.abbandonaAzienda().subscribe({
      next: () => {
        this.authService.logout();
        this.router.navigate(['/login']);

      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    });
  }


  inviaModifiche(dati: any) {
    this.impostazioniService.inviaModifiche(dati).subscribe({
      next: () => {
        alert("Profilo aggiornato!");
        this.caricaDatiUtente();
      },
      error: (err) => this.gestisciErrore(err.error),
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
