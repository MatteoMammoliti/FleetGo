import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {TemplateFinestraModale} from '@shared/Componenti/Modali/template-finestra-modale/template-finestra-modale';
import {MessaggioCardVuota} from '@shared/Componenti/Banner/messaggio-card-vuota/messaggio-card-vuota';

@Component({
  selector: 'app-affiliazione-azienda',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TemplateTitoloSottotitolo,
    TemplateFinestraModale,
    MessaggioCardVuota
  ],
  templateUrl: './affiliazione-azienda.html',
  styleUrl: './affiliazione-azienda.css',
})
export class AffiliazioneAzienda {

  @Input() datiAzienda: any = {};
  @Output() inviaRichiestaDisdetta = new EventEmitter();

  mostraModaleConferma: boolean = false;

  apriModaleDisdetta() {
    this.mostraModaleConferma = true;
  }

  confermaDisdetta() {
    this.inviaRichiestaDisdetta.emit();
    this.mostraModaleConferma = false;
  }
}
