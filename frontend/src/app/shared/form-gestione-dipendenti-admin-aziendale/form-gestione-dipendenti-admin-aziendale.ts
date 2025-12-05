import {Component, inject, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TabellaBackground } from '@shared/tabella-background/tabella-background';
import {DipendenteDTO} from '@models/dipendenteDTO.models';
import {DipendentiService} from '@core/services/ServiceSezioneAdminAziendale/dipendenti-service';

@Component({
  selector: 'app-form-gestione-dipendenti-admin-aziendale',
  imports: [FormsModule, TabellaBackground],
  standalone: true,
  templateUrl: './form-gestione-dipendenti-admin-aziendale.html',
  styleUrl: './form-gestione-dipendenti-admin-aziendale.css',
})
export class FormGestioneDipendentiAdminAziendale implements OnInit {
  cercaDipendente: string = '';

  private service: DipendentiService = inject(DipendentiService);

  listaDipendenti: DipendenteDTO[] = [];

  ngOnInit() {
    this.getDipendenti();
  }

  getDipendenti() {
    this.service.getDipendenti().subscribe({
      next: (response: DipendenteDTO[]) => {
        this.listaDipendenti = response;
      },
      error: (error) => {
        console.error('Errore durante il recupero dei dipendenti:', error);
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
}
