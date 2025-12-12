import {Component, Input} from '@angular/core';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-tabella-manutenzioni-in-corso',
  imports: [
    DatePipe
  ],
  templateUrl: './tabella-manutenzioni-in-corso.html',
  styleUrl: './tabella-manutenzioni-in-corso.css',
})
export class TabellaManutenzioniInCorso {

  @Input() listaRichieste:RichiestaManutenzioneDTO[]=[]

  onConcludi(){}

}
