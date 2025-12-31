import {Component, EventEmitter, Input, model, Output} from '@angular/core';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {CommonModule, DatePipe} from '@angular/common';

@Component({
  selector: 'app-card-offerta',
  standalone: true,
  imports: [
    DatePipe,
    CommonModule
  ],
  templateUrl: './card-offerta.html',
  styleUrl: './card-offerta.css',
})
export class CardOfferta {

  @Input() offerta!: OffertaDTO;
  @Output() eliminaOfferta = new EventEmitter<number>();


  elimina() {
    this.eliminaOfferta.emit(this.offerta.idOfferta);
  }
}
