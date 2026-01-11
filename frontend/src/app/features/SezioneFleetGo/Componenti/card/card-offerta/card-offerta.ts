import {Component, EventEmitter, Input, Output} from '@angular/core';
import {OffertaDTO} from '@core/models/OffertaDTO';
import {CommonModule, DatePipe} from '@angular/common';
import {TemplateCardConRiga} from '@shared/Componenti/Card/template-card-con-riga/template-card-con-riga';

@Component({
  selector: 'app-card-offerta',
  standalone: true,
  imports: [
    DatePipe,
    CommonModule,
    TemplateCardConRiga
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
