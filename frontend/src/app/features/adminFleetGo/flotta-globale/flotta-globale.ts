import {Component} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {FormBackground} from '@shared/form-background/form-background';
import {TabellaAuto} from '@shared/tabella-auto/tabella-auto';
import {FormAggiungiAuto} from '@shared/form-aggiungi-auto/form-aggiungi-auto';



@Component({
  selector: 'app-flotta-globale',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    FormBackground,
    TabellaAuto,
    FormAggiungiAuto
  ],
  templateUrl: './flotta-globale.html',
  styleUrl: './flotta-globale.css',
})
export class FlottaGlobale {

}
