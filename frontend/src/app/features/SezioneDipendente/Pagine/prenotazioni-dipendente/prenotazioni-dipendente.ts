import { Component } from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {PrenotazioneCard} from '@features/SezioneDipendente/componenti/prenotazione-card/prenotazione-card';
import {PrenotazioniService} from '@features/SezioneDipendente/ServiceSezioneDipendente/prenotazioni-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-prenotazioni-dipendente',
  imports: [
    PrenotazioneCard
  ],
  templateUrl: './prenotazioni-dipendente.html',
  styleUrl: './prenotazioni-dipendente.css',
})
export class PrenotazioniDipendente {
  constructor(private service:PrenotazioniService,private router:Router) {
  }
  prenotazioni:RichiestaNoleggioDTO[]=[];

  ngOnInit(){
    this.getRichiesteDipendente();
  }

  getRichiesteDipendente(){
    this.service.richiediPrenotazioniDipendente().subscribe({
      next:(risposta:RichiestaNoleggioDTO[])=>{
        this.prenotazioni=risposta
    },
      error:(err)=>console.log("Errore nel caricare i noleggi dei dipendente")
    });
  }

  clickNuovaPrenotazione(){
    this.router.navigate(['/dashboardDipendente/nuovaPrenotazione'])
  }

}
