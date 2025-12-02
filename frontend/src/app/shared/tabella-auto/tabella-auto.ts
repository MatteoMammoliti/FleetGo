import { Component } from '@angular/core';
import {FormAutenticazione} from "@shared/form-autenticazione/form-autenticazione";
import {VeicoloDTO} from '@models/veicoloDTO.model';

@Component({
  selector: 'app-tabella-auto',
    imports: [
        FormAutenticazione
    ],
  templateUrl: './tabella-auto.html',
  styleUrl: './tabella-auto.css',
})
export class TabellaAuto {
  constructor() {

  }

  listaVeicoli: VeicoloDTO[] = [];


}
