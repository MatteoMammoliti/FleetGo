import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-tabella-aziende',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tabella-aziende.html',
  styleUrl: './tabella-aziende.css'
})

export class TabellaAziendeComponent {

  @Input() listaAziende:AziendaDTO[] = [];
  @Input() modalitaArchivio = false;
  @Output() riabilitaAzienda = new EventEmitter<number>();
  @Output() disabilitaAzienda = new EventEmitter<number>();

}
