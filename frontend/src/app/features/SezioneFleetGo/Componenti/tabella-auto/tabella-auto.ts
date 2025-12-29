import {Component, Input,Output, EventEmitter} from '@angular/core';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {FormsModule} from '@angular/forms';
import {IconaStato} from '@shared/Componenti/Ui/icona-stato/icona-stato';

@Component({
  selector: 'app-tabella-auto',
  imports: [
    FormsModule,
    IconaStato
  ],
  templateUrl: './tabella-auto.html',
  styleUrl: './tabella-auto.css',
})
export class TabellaAuto {
  @Input() listaVeicoli: VeicoloDTO[] = [];
  @Input() listaAziende: AziendaDTO[] = [];
  @Output() richiestaEliminazione = new EventEmitter<string>();
  @Output() apriInfoVeicolo=new EventEmitter<string>();

  eliminaVeicolo(targaVeicolo: string | undefined) {
    if(targaVeicolo) this.richiestaEliminazione.emit(targaVeicolo);
  }

  getBadgeStatus(auto: any) {
    if (auto.inManutenzione || auto.statusContrattualeVeicolo === 'In Manutenzione') {
      return 'rosso';
    } else if (auto.statusContrattualeVeicolo === 'Disponibile') {
      return 'verde';
    }
    return 'giallo';
  }

}
