import {Component, Input} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-prossimo-viaggio',
  imports: [
    DatePipe
  ],
  standalone:true,
  templateUrl: './prossimo-viaggio.html',
  styleUrl: './prossimo-viaggio.css',
})
export class ProssimoViaggio {

  @Input() viaggio:RichiestaNoleggioDTO | undefined;

}
