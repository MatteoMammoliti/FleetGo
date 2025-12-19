import { Component } from '@angular/core';
import {ProfiloPersonale} from '@features/SezioneDipendente/componenti/profilo-personale/profilo-personale';
import {NgClass} from '@angular/common';
import {AffiliazioneAzienda} from '@features/SezioneDipendente/componenti/affiliazione-azienda/affiliazione-azienda';
import {UtenteDTO} from '@core/models/utenteDTO.model';
import {ImpostazioniService} from '@features/SezioneDipendente/ServiceSezioneDipendente/impostazioni-service';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';

@Component({
  selector: 'app-impostazioni-dipendente',
  imports: [
    ProfiloPersonale,
    NgClass,
    AffiliazioneAzienda
  ],
  templateUrl: './impostazioni-dipendente.html',
  styleUrl: './impostazioni-dipendente.css',
})
export class ImpostazioniDipendente {
  constructor(private service:ImpostazioniService) {}
  tabSelezionata:string="";
  sezione:string="";
  utente!:ModificaDatiUtenteDTO;

  ngOnInit(){
    this.tabSelezionata="Profilo"
    this.sezione="Profilo"
    this.getDipendente()
  }

  cambiaTab(categoria: string) {
    this.tabSelezionata=categoria;
    this.sezione=categoria;
  }

  getDipendente(){
    this.service.getDipendente().subscribe({
      next:(risposta:ModificaDatiUtenteDTO)=>{
        this.utente=risposta;
      },
      error:(err)=>console.error("Errore nel caricare l'utente.")
    })
  }
  inviaModifiche(dati:ModificaDatiUtenteDTO){
    this.service.inviaModifiche(dati).subscribe({
      next:(risposta:any)=>{
        this.getDipendente()
      }
    })
  }
}
