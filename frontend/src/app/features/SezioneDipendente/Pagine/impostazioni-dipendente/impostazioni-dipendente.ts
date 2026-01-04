import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IntestazioneEBackground } from '@shared/Componenti/Ui/intestazione-ebackground/intestazione-ebackground';
import { ProfiloPersonale } from '../../componenti/profilo-personale/profilo-personale';
import { PatenteDocumentiComponent } from '../../componenti/patente-documenti/patente-documenti';
import { AffiliazioneAzienda } from '../../componenti/affiliazione-azienda/affiliazione-azienda';
import { ImpostazioniService } from '../../ServiceSezioneDipendente/impostazioni-service';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';
import {AuthService} from '@core/auth/auth-service';
import {Router, RouterLink} from '@angular/router';

@Component({
  selector: 'app-impostazioni-dipendente',
  standalone: true,
  imports: [
    CommonModule,
    IntestazioneEBackground,
    ProfiloPersonale,
    PatenteDocumentiComponent,
    AffiliazioneAzienda
  ],
  templateUrl: './impostazioni-dipendente.html',
  styleUrl: './impostazioni-dipendente.css',
})

export class ImpostazioniDipendenteComponent implements OnInit {

  tabSelezionata: string = 'Profilo';
  sezione: string = 'Profilo';
  utente: ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO;
  urlPatente : string = '';

  constructor(private impostazioniService: ImpostazioniService,private authService:AuthService,private router:Router) {}

  ngOnInit() {
    this.caricaDatiUtente();
  }

  caricaDatiUtente() {
    this.impostazioniService.getDipendente().subscribe({
      next: (datiServer) => {
        if(datiServer){
          this.utente = datiServer;
          console.log(this.utente);
        }
      },
      error: (err) => console.error("Errore caricamento dati:", err)
    });

    this.impostazioniService.getUrlPatente().subscribe({
      next: (urlPatenteServer) => {
        this.urlPatente = urlPatenteServer;
      }, error: err => console.error("Errore caricamento dati:", err)
    })
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
      }, error: err => console.error("Errore caricamento dati:", err)
    })
  }

  inviaDisdetta(){
    this.impostazioniService.abbandonaAzienda().subscribe({
      next: () => {
        this.authService.logout();
        this.router.navigate(['/login']);

      },
      error: (err) => {
        console.error(err);
        alert("Errore durante la disdetta.");
      }
    });
  }


  inviaModifiche(dati: any) {
    this.impostazioniService.inviaModifiche(dati).subscribe({
      next: () => {
        alert("Profilo aggiornato!");
        this.caricaDatiUtente();
      },
      error: (err) => console.error(err)
    });
  }
}
