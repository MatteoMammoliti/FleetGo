import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';
import { RouterLink } from '@angular/router';
import {CardDipendente} from '@features/SezioneAdminAziendale/Componenti/card/card-dipendente/card-dipendente';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
import {InputChecked} from '@shared/Componenti/Ui/input-checked/input-checked';

@Component({
  selector: 'app-elenco-dipendenti',
  imports: [FormsModule, CardDipendente, MessaggioCardVuota, InputChecked],
  standalone: true,
  templateUrl: './elenco-dipendenti.html',
  styleUrl: './elenco-dipendenti.css',
})
export class ElencoDipendenti {
  cercaDipendente: string = '';
  @Input() listaDipendenti: DipendenteDTO[] = [];
  @Output() richiestaRimozioneDipendente= new EventEmitter<number>();
  @Output() apriDettagliDipendente = new EventEmitter<DipendenteDTO>();

  get dipendentiFiltrati(): DipendenteDTO[] {
    if (!this.cercaDipendente) {
      return this.listaDipendenti;
    }

    return this.listaDipendenti.filter(d =>
      (d.nomeUtente && d.nomeUtente.toLowerCase().includes(this.cercaDipendente.toLowerCase())) ||
      (d.cognomeUtente && d.cognomeUtente.toLowerCase().includes(this.cercaDipendente.toLowerCase()))
    );
  }

  rimuoviDipendente(idDipendente:number | undefined) {
    this.richiestaRimozioneDipendente.emit(idDipendente);
  }

  apriDettagli(dipendente: DipendenteDTO) {
    this.apriDettagliDipendente.emit(dipendente);
  }

}
