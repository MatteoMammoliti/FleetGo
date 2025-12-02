import {Component} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {FormBackground} from '@shared/form-background/form-background';
import {TabellaAuto} from '@shared/tabella-auto/tabella-auto';
import {FormCercaAuto} from '@shared/form-cerca-auto/form-cerca-auto';



@Component({
  selector: 'app-flotta-globale',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    FormBackground,
    TabellaAuto,
    FormCercaAuto
  ],
  templateUrl: './flotta-globale.html',
  styleUrl: './flotta-globale.css',
})
export class FlottaGlobale {

}
