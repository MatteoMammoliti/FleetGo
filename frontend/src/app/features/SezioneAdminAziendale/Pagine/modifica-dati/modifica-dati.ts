import {Component, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {FormModificaDatiAdminAziendale} from '@features/SezioneAdminAziendale/Componenti/form-modifica-dati-admin-aziendale/form-modifica-dati-admin-aziendale';
import {ModificaDatiService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/modifica-dati-service';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {
  MappaGestioneLuoghi
} from '@features/SezioneAdminAziendale/Componenti/mappa-gestione-luoghi/mappa-gestione-luoghi';
import {
  TabellaGestioneLuoghi
} from '@features/SezioneAdminAziendale/Componenti/tabella-gestione-luoghi/tabella-gestione-luoghi';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-modifica-dati',
  standalone: true,
  imports: [
    FormsModule,
    FormModificaDatiAdminAziendale,
    MappaGestioneLuoghi,
    TabellaGestioneLuoghi,
    NgClass
  ],
  templateUrl: './modifica-dati.html',
  styleUrl: './modifica-dati.css'
})


export class ModificaDatiComponent implements OnInit{

  constructor(private service: ModificaDatiService) {}

  errore:string="";
  utenteDaModificare: ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO;
  luoghiEsistenti: LuogoDTO[] = [];
  modalitaAggiunta = false;

  ngOnInit(){
    this.caricaDatiAdmin();
    this.caricaLuoghiEsistenti();
  }

  iniziaAggiuntaLuogo() {
    this.modalitaAggiunta = !this.modalitaAggiunta;
  }

  aggiungiLuogo(luogo: LuogoDTO) {
    this.service.aggiungiLuogo(luogo).subscribe({
      next: value => {
        this.luoghiEsistenti.push(luogo);
        this.iniziaAggiuntaLuogo();
        this.caricaLuoghiEsistenti();
      }
    })
  }

  modificaDatiUtente(datiAggiornati:ModificaDatiUtenteDTO){
    this.service.modificaDati(datiAggiornati).subscribe({
      next: (res) => {
        if(res) {
          this.caricaDatiAdmin();
        }
        this.errore = 'Dati modificati con successo';
      },
      error: (err) => {
        console.log('Errore durante la modifica dati', err);
      }
    });
  }

  caricaDatiAdmin() {
    this.service.getDati().subscribe({
      next: (datiRicevuti) => {

        console.log("Dati arrivati dal backend:", datiRicevuti);

        if(datiRicevuti) {
          this.utenteDaModificare=datiRicevuti;
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento dati', err);
        this.errore = 'Impossibile caricare i dati utente';
      }
    });
  }

  caricaLuoghiEsistenti() {
    this.service.getLuoghi().subscribe({
      next: value => {
        this.luoghiEsistenti = [...value];
        console.log("luoghi ricevuti:", value);
      },
      error: err => {
        console.log(err);
      }
    });
  }

  impostaSede(idLuogo: number) {
    this.service.impostaSedeAzienda(idLuogo).subscribe({
      next: value => {
        if(value) {
          this.caricaDatiAdmin();
          this.caricaLuoghiEsistenti();
        }
      }, error: err => {
        console.log(err);
      }
    })
  }

  eliminaLuogo(idLuogo: number) {
    this.service.eliminaLuogo(idLuogo).subscribe({
      next: value => {
        if(value) {
          this.caricaDatiAdmin();
          this.caricaLuoghiEsistenti();
        }
      }, error: err => {
        console.log(err);
      }
    })
  }
}
