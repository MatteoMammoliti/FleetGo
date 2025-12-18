import {Component, OnInit} from '@angular/core';
import { ElencoDipendenti } from '@features/SezioneAdminAziendale/Componenti/form-gestione-dipendenti-admin-aziendale/elenco-dipendenti';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';
import {DipendentiService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dipendenti-service';
import {
  ModaleDettagliDipendente
} from '@features/SezioneAdminAziendale/Componenti/modale-dettagli-dipendente/modale-dettagli-dipendente';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';

@Component({
  selector: 'app-gestione-dipendenti',
  standalone: true,
  imports: [ElencoDipendenti, ModaleDettagliDipendente],
  templateUrl: './gestione-dipendenti.html',
  styleUrl: './gestione-dipendenti.css'
})

export class GestioneDipendentiComponent implements OnInit{

  constructor(private service: DipendentiService) { }

  ngOnInit() { this.getDipendenti();}

  listaDipendentiAzienda: DipendenteDTO[] = [];
  richiesteNoleggio: RichiestaNoleggioDTO[] = [];

  modaleDettaglioDipendenteVisibile = false;
  dipendenteDaVisualizzare: DipendenteDTO = {} as DipendenteDTO;

  getDipendenti() {
    this.service.getDipendenti().subscribe({
      next: (response: DipendenteDTO[]) => {
        this.listaDipendentiAzienda = response;
      },
      error: (error) => {
        console.error('Errore durante il recupero dei dipendenti:', error);
      }
    })
  }

  getRichiesteNoleggio(idDipendente: number | undefined) {

    if(!idDipendente){
      this.richiesteNoleggio = [];
      return;
    }

    this.service.getRichiesteNoleggio(idDipendente).subscribe({
      next: value => {
        if(value) this.richiesteNoleggio = value;
      }, error: err => {
        console.error(err);
        this.richiesteNoleggio = [];
      }
    })
  }

  rimuoviDipendente(idDipendente:number | undefined) {

    this.service.rimuoviDipendente(idDipendente).subscribe({
      next: (response) => {
        this.getDipendenti();
      },
      error: (error) => {
        console.error('Errore durante la rimozione del dipendente:', error);
      }
    });
  }

  apriDettagliDipendente(dipendente: DipendenteDTO) {
    this.dipendenteDaVisualizzare = dipendente;
    this.modaleDettaglioDipendenteVisibile = true;
    this.getRichiesteNoleggio(dipendente.idUtente);
  }

  chiudiModale() {
    this.modaleDettaglioDipendenteVisibile = false;
  }

  approvaPatente(idUtente: number) {
    this.service.approvaPatente(idUtente).subscribe({
      next: value => {
        if(value) {
          setInterval( () => {this.dipendenteDaVisualizzare.patenteAccettata = true}, 3000);
        }
      }
    })
  }
}
