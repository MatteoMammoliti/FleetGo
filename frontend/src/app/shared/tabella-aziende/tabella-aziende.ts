import { Component } from '@angular/core';
import {TabellaBackground} from '@shared/tabella-background/tabella-background';

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

  listaAziende = [
    { nome: 'Azienda1', piva: '11111111111', sede: 'Bivona' },
    { nome: 'Azienda2', piva: '22222222222', sede: 'Vibo Marina'},
    { nome: 'Azienda3', piva: '33333333333', sede: 'Porto Salvo' }
  ];

  elimina(azienda: any) {
    alert("elimino: " + azienda.nome);
  }

}
