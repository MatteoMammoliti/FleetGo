import {Component, Input} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-prenotazione-card',
  imports: [
    NgClass
  ],
  templateUrl: './prenotazione-card.html',
  styleUrl: './prenotazione-card.css',
})
export class PrenotazioneCard {
  @Input() prenotazione:RichiestaNoleggioDTO ={} as RichiestaNoleggioDTO;

}
