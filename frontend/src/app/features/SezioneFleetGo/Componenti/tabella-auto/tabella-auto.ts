import {Component, Input,Output, EventEmitter} from '@angular/core';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-tabella-auto',
  imports: [
    FormsModule
  ],
  templateUrl: './tabella-auto.html',
  styleUrl: './tabella-auto.css',
})
export class TabellaAuto {
  @Input() listaVeicoli: VeicoloDTO[] = [];
  @Input() listaAziende: AziendaDTO[] = [];
  @Output() richiestaEliminazione = new EventEmitter<string>();
  @Output() apriInfoVeicolo=new EventEmitter<string>();


  eliminaVeicolo(targaVeicolo: string) {
    this.richiestaEliminazione.emit(targaVeicolo);
  }
}
