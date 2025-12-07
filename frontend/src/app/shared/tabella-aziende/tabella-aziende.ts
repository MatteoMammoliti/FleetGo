import {Component, EventEmitter, inject, Input, Output, SimpleChanges} from '@angular/core';
import {TabellaBackground} from '@shared/tabella-background/tabella-background';
import {AziendeAffiliateService} from '@core/services/ServiceSezioneFleetGo/aziende-affiliate-service';
import {AziendaDTO} from '@models/aziendaDTO';

@Component({
  selector: 'app-tabella-aziende',
  standalone: true,
  imports: [
    TabellaBackground
  ],
  templateUrl: './tabella-aziende.html',
  styleUrl: './tabella-aziende.css'
})
export class TabellaAziendeComponent {

  @Input() listaAziende:AziendaDTO[] = [];
  @Output() richiediEliminazione = new EventEmitter<number>();


  elimina(idAdmin: number | undefined) {
    console.log("sono in elimina figlio" + idAdmin);
    this.richiediEliminazione.emit(idAdmin);
  }
}
