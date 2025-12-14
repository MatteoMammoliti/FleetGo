import {Component, Input} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';

@Component({
  selector: 'app-prossimo-viaggio',
  imports: [],
  standalone:true,
  templateUrl: './prossimo-viaggio.html',
  styleUrl: './prossimo-viaggio.css',
})
export class ProssimoViaggio {

  @Input() viaggio:RichiestaNoleggioDTO | undefined;

}
