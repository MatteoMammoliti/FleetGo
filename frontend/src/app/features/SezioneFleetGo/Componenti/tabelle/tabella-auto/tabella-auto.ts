import {Component, EventEmitter, Input, Output} from '@angular/core';
import {VeicoloDTO} from '@core/models/VeicoloDTO';
import {AziendaDTO} from '@core/models/AziendaDTO';
import {FormsModule} from '@angular/forms';
import {IconaStato} from '@shared/Componenti/Banner/icona-stato/icona-stato';
import {MessaggioCardVuota} from '@shared/Componenti/Banner/messaggio-card-vuota/messaggio-card-vuota';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';

@Component({
  selector: 'app-tabella-auto',
  imports: [
    FormsModule,
    IconaStato,
    MessaggioCardVuota
  ],
  templateUrl: './tabella-auto.html',
  styleUrl: './tabella-auto.css',
  animations: [ANIMAZIONE_TABELLA]
})
export class TabellaAuto {
  @Input() listaVeicoli: VeicoloDTO[] | null = null;
  @Input() listaAziende: AziendaDTO[] = [];
  @Output() richiestaEliminazione = new EventEmitter<string>();
  @Output() apriInfoVeicolo = new EventEmitter<string>();

  eliminaVeicolo(targaVeicolo: string | undefined) {
    if (targaVeicolo) this.richiestaEliminazione.emit(targaVeicolo);
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
