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
  daVisualizzare:RichiestaNoleggioDTO[]=[]
  filtroAttivo:string="Tutte"

  ngOnInit(){
    this.getRichiesteDipendente();
  }

  getRichiesteDipendente(){
    this.service.richiediPrenotazioniDipendente().subscribe({
      next:(risposta:RichiestaNoleggioDTO[])=>{
        this.prenotazioni=risposta
        this.daVisualizzare=risposta
    },
      error:(err)=>console.log("Errore nel caricare i noleggi dei dipendente")
    });
  }

  clickNuovaPrenotazione(){
    this.router.navigate(['/dashboardDipendente/nuovaPrenotazione'])
  }

  impostaFiltro(categoria: string) {
    this.filtroAttivo=categoria;
    if(categoria==="Tutte"){
      this.daVisualizzare=this.prenotazioni;
    }else if (categoria==="Terminata"){
      this.daVisualizzare = this.prenotazioni.filter(p => p.statoRichiesta === 'Terminata');
    }else if (categoria==="In corso"){
      this.daVisualizzare=this.prenotazioni.filter(p=>p.statoRichiesta==='In corso');
    }else if(categoria==="Da ritirare"){
      this.daVisualizzare=this.prenotazioni.filter(p=>p.statoRichiesta==='Da ritirare');
    }


  }
}
